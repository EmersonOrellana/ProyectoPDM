package com.example.proyectopdm

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class NuevoRolFragment : Fragment(R.layout.fragment_nuevo_rol) {

    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Vinculamos de forma exacta tus EditTexts y Botones del XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreNuevoRol)
        val etCodigo = view.findViewById<EditText>(R.id.etCodigoNuevoRol)
        val etNotas = view.findViewById<EditText>(R.id.etNotasNuevoRol)

        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarNuevoRol)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarNuevoRol)

        // Función local para regresar al listado de roles
        fun regresarAListaRoles() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RolesListaFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // Evento de clic en Cancelar
        btnCancelar.setOnClickListener {
            regresarAListaRoles()
        }

        // 2. LÓGICA DE INSERCIÓN DINÁMICA (SQLITE INSERT)
        btnConfirmar.setOnClickListener {
            val nombreTxt = etNombre.text.toString().trim()
            val codigoTxt = etCodigo.text.toString().trim()
            val notasTxt = etNotas.text.toString().trim()

            // Validación de campos obligatorios
            if (nombreTxt.isEmpty() || codigoTxt.isEmpty() || notasTxt.isEmpty()) {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de seguridad: Asegurar que el código sea entero válido
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

                // Ejecutamos el insert en la tabla ROL
                val resultadoId = db.insert("ROL", null, valores)

                if (resultadoId != -1L) {
                    Toast.makeText(context, "¡Rol '$nombreTxt' registrado con éxito!", Toast.LENGTH_SHORT).show()
                    regresarAListaRoles()
                } else {
                    Toast.makeText(context, "Error al insertar el rol en SQLite", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error BD: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }

        // Interceptar el gesto de retroceso del teléfono
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAListaRoles()
                }
            }
        )
    }
}