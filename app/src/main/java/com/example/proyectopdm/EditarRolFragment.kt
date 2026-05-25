package com.example.proyectopdm

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class EditarRolFragment : Fragment(R.layout.fragment_editar_rol) {

    private lateinit var dbHelper: DatabaseHelper
    private var idRolAEditar: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Capturamos el ID del rol enviado desde el RolesListaFragment
        idRolAEditar = arguments?.getInt("id_rol") ?: -1

        // 2. Vinculamos de forma exacta tus IDs del XML de edición
        val etNombre = view.findViewById<EditText>(R.id.etNombreEditarRol)
        val etCodigo = view.findViewById<EditText>(R.id.etCodigoEditarRol)
        val etNotas = view.findViewById<EditText>(R.id.etNotasEditarRol)

        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarEditarRol)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditarRol)

        // Función local para regresar al listado de roles
        fun regresarAListaRoles() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RolesListaFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // 3. CARGAR LOS DATOS ACTUALES DEL ROL SELECCIONADO
        if (idRolAEditar != -1) {
            cargarDatosDelRol(idRolAEditar, etNombre, etCodigo, etNotas)
        } else {
            Toast.makeText(context, "Error: No se recibió credencial de edición", Toast.LENGTH_SHORT).show()
        }

        // Evento de clic en Cancelar
        btnCancelar.setOnClickListener {
            regresarAListaRoles()
        }

        // 4. LÓGICA DEL BOTÓN CONFIRMAR (SQLITE UPDATE)
        btnConfirmar.setOnClickListener {
            val nombreTxt = etNombre.text.toString().trim()
            val codigoTxt = etCodigo.text.toString().trim()
            val notasTxt = etNotas.text.toString().trim()

            // Validaciones de seguridad
            if (nombreTxt.isEmpty() || codigoTxt.isEmpty() || notasTxt.isEmpty()) {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val codigoEntero = codigoTxt.toIntOrNull()
            if (codigoEntero == null) {
                Toast.makeText(context, "El código debe ser un número entero válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.openDatabase()
            try {
                // Sincronización idéntica con las mayúsculas de tu captura SQL
                val valores = ContentValues().apply {
                    put("NOMBRE_ROL", nombreTxt)
                    put("CODIGO_ROL", codigoEntero)
                    put("DESCRIPCION_ROL", notasTxt)
                }

                // Ejecutamos la actualización filtrando por la llave primaria ID_ROL
                val filasAfectadas = db.update(
                    "ROL",
                    valores,
                    "ID_ROL = ?",
                    arrayOf(idRolAEditar.toString())
                )

                if (filasAfectadas > 0) {
                    Toast.makeText(context, "¡Rol actualizado exitosamente!", Toast.LENGTH_SHORT).show()
                    regresarAListaRoles()
                } else {
                    Toast.makeText(context, "No se pudieron guardar los cambios", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error BD: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }

        // Interceptar el gesto de retroceso físico del teléfono
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAListaRoles()
                }
            }
        )
    }

    // 🔍 CONSULTA SELECT PREVIA PARA LLENAR EL FORMULARIO
    private fun cargarDatosDelRol(
        idRol: Int,
        etNombre: EditText,
        etCodigo: EditText,
        etNotas: EditText
    ) {
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            val query = "SELECT * FROM ROL WHERE ID_ROL = ?"
            cursor = db.rawQuery(query, arrayOf(idRol.toString()))

            if (cursor != null && cursor.moveToFirst()) {
                // Rellenamos las cajas usando los campos exactos de tu imagen
                etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL")) ?: "")
                etCodigo.setText(cursor.getInt(cursor.getColumnIndexOrThrow("CODIGO_ROL")).toString())
                etNotas.setText(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_ROL")) ?: "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al cargar el rol: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}