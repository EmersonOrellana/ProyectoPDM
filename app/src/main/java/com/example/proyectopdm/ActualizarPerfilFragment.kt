package com.example.proyectopdm

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class ActualizarPerfilFragment : Fragment(R.layout.fragment_actualizar_perfil) {

    private lateinit var dbHelper: DatabaseHelper
    private var correoUsuarioLogueado: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        correoUsuarioLogueado = arguments?.getString("correo_usuario")

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etApellidos = view.findViewById<EditText>(R.id.etApellidos)
        val etUser = view.findViewById<EditText>(R.id.etUser)      // Campo de DUI
        val etNit = view.findViewById<EditText>(R.id.etNit)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefono)
        val etRol = view.findViewById<EditText>(R.id.etRol)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreo)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackPerfil)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarPerfil)

        // 💥 FORMATEADOR AUTOMÁTICO PARA EL DUI (etUser)
        etUser.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                // Quitamos guiones viejos para formatear desde cero limpio
                val numeros = s.toString().replace("-", "")
                val sb = StringBuilder()

                for (i in numeros.indices) {
                    sb.append(numeros[i])
                    // Al llegar al dígito 8, le clavamos el guion de forma automática
                    if (i == 7 && numeros.length > 8) {
                        sb.append("-")
                    }
                }

                etUser.setText(sb.toString())
                etUser.setSelection(sb.length) // Mueve el cursor siempre al final
                isUpdating = false
            }
        })

        // 💥 FORMATEADOR AUTOMÁTICO PARA EL NIT (etNit)
        etNit.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                val numeros = s.toString().replace("-", "")
                val sb = StringBuilder()

                for (i in numeros.indices) {
                    sb.append(numeros[i])
                    // Aplicamos guiones según las posiciones del NIT oficial (4-6-3-1)
                    if (i == 3 && numeros.length > 4) sb.append("-")
                    else if (i == 9 && numeros.length > 10) sb.append("-")
                    else if (i == 12 && numeros.length > 13) sb.append("-")
                }

                etNit.setText(sb.toString())
                etNit.setSelection(sb.length)
                isUpdating = false
            }
        })

        // Cargar datos iniciales
        if (!correoUsuarioLogueado.isNullOrEmpty()) {
            cargarDatosParaEditar(correoUsuarioLogueado!!, etNombre, etApellidos, etUser, etNit, etFecha, etTelefono, etRol, etCorreo)
        }

        fun regresarAVerPerfil() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, VerPerfilFragment())
                .commit()
            try {
                activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
            } catch (e: Exception) { e.printStackTrace() }
        }

        btnBack.setOnClickListener { regresarAVerPerfil() }

        btnActualizar.setOnClickListener {
            val nombreTxt = etNombre.text.toString().trim()
            val apellidoTxt = etApellidos.text.toString().trim()
            val duiTxt = etUser.text.toString().trim()
            val nitTxt = etNit.text.toString().trim()
            val fechaTxt = etFecha.text.toString().trim()
            val telefonoTxt = etTelefono.text.toString().trim()
            val correoTxt = etCorreo.text.toString().trim()

            if (nombreTxt.isEmpty() || apellidoTxt.isEmpty() || correoTxt.isEmpty()) {
                Toast.makeText(context, "Campos obligatorios: Nombre, Apellido y Correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🚨 VALIDACIÓN DE LONGITUD DE CARACTERES CON GUIONES INCLUIDOS
            if (duiTxt.isNotEmpty() && duiTxt.length < 10) {
                Toast.makeText(context, "El DUI debe tener 9 dígitos y su guion (Formato: 00000000-0)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (nitTxt.isNotEmpty() && nitTxt.length < 17) {
                Toast.makeText(context, "El NIT debe estar completo (Formato de 14 dígitos con guiones)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val db = dbHelper.openDatabase()
            try {
                val valores = ContentValues().apply {
                    put("NOMBRE_USUARIO", nombreTxt)
                    put("APELLIDO_USUARIO", apellidoTxt)
                    put("DUI_USUARIO", duiTxt)
                    put("NIT_USUARIO", nitTxt)
                    put("FECHA_CONTRATACION", fechaTxt)
                    put("TELEFONO_USUARIO", telefonoTxt)
                    put("CORREO_ELECTRONICO", correoTxt)
                }

                val correoFiltro = if (!correoUsuarioLogueado.isNullOrEmpty()) correoUsuarioLogueado else correoTxt

                val filasAfectadas = db.update("USUARIO", valores, "CORREO_ELECTRONICO = ?", arrayOf(correoFiltro))

                if (filasAfectadas > 0) {
                    Toast.makeText(context, "¡Perfil guardado con sus validaciones!", Toast.LENGTH_SHORT).show()
                    regresarAVerPerfil()
                } else {
                    Toast.makeText(context, "No se encontró registro para actualizar.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { regresarAVerPerfil() }
        })
    }

    private fun cargarDatosParaEditar(
        correo: String, etNombre: EditText, etApellidos: EditText, etUser: EditText,
        etNit: EditText, etFecha: EditText, etTelefono: EditText, etRol: EditText, etCorreo: EditText
    ) {
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            val query = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ?"
            cursor = db.rawQuery(query, arrayOf(correo))

            if (cursor != null && cursor.moveToFirst()) {
                etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")) ?: "")
                etApellidos.setText(cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO_USUARIO")) ?: "")
                etCorreo.setText(cursor.getString(cursor.getColumnIndexOrThrow("CORREO_ELECTRONICO")) ?: "")
                etUser.setText(cursor.getString(cursor.getColumnIndexOrThrow("DUI_USUARIO")) ?: "")
                etNit.setText(cursor.getString(cursor.getColumnIndexOrThrow("NIT_USUARIO")) ?: "")
                etFecha.setText(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CONTRATACION")) ?: "")
                etTelefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_USUARIO")) ?: "")

                val idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                etRol.setText(when(idRol) {
                    1 -> "Administrador"
                    2 -> "Usuario Operativo"
                    else -> "Personal"
                })
            }
        } catch (e: Exception) { e.printStackTrace() } finally { cursor?.close(); db.close() }
    }
}