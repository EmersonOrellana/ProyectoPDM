package com.example.proyectopdm

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class RegistrarPersonalFragment : Fragment(R.layout.fragment_registrar_personal) {

    private lateinit var dbHelper: DatabaseHelper

    // 💥 MAPA PARA RELACIONAR EL NOMBRE DEL ROL CON SU ID REAL DE LA BASE DE DATOS
    private val mapaRoles = HashMap<String, Int>()
    private val listaNombresRoles = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Mapeo de vistas con sus IDs del XML
        val etNombre = view.findViewById<EditText>(R.id.etRegNombre)
        val etApellidos = view.findViewById<EditText>(R.id.etRegApellidos)
        val etDui = view.findViewById<EditText>(R.id.etRegDui)
        val etNit = view.findViewById<EditText>(R.id.etRegNit)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)
        val etTelefono = view.findViewById<EditText>(R.id.etRegTelefono)
        val etCorreo = view.findViewById<EditText>(R.id.etRegCorreo)
        val etPassword = view.findViewById<EditText>(R.id.etRegPassword)
        val autoCompleteRol = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteRol)

        // 💥 2. CARGA DINÁMICA DE ROLES DESDE SQLITE
        obtenerRolesDesdeBD()

        // Configuramos el adaptador usando la lista dinámica recuperada de la BD
        val adapterRoles = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listaNombresRoles)
        autoCompleteRol.setAdapter(adapterRoles)

        // 🗓️ DATEPICKER AUTOMÁTICO PARA LA FECHA DE CONTRATACIÓN
        etFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val fechaFormateada = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etFecha.setText(fechaFormateada)
            }, year, month, day)
            datePickerDialog.show()
        }

        // 💥 MÁSCARA EN TIEMPO REAL PARA EL DUI
        etDui.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val str = s.toString().replace("-", "")
                val sb = StringBuilder()
                for (i in str.indices) {
                    sb.append(str[i])
                    if (i == 7 && str.length > 8) sb.append("-")
                }
                etDui.setText(sb.toString())
                etDui.setSelection(sb.length)
                isUpdating = false
            }
        })

        // 💥 MÁSCARA EN TIEMPO REAL PARA EL NIT
        etNit.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val str = s.toString().replace("-", "")
                val sb = StringBuilder()
                for (i in str.indices) {
                    sb.append(str[i])
                    if (i == 3 && str.length > 4) sb.append("-")
                    else if (i == 9 && str.length > 10) sb.append("-")
                    else if (i == 12 && str.length > 13) sb.append("-")
                }
                etNit.setText(sb.toString())
                etNit.setSelection(sb.length)
                isUpdating = false
            }
        })

        // Botón Cancelar
        view.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 💾 BOTÓN CREAR USUARIO
        view.findViewById<Button>(R.id.btnCrearUsuario).setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellidos.text.toString().trim()
            val dui = etDui.text.toString().trim()
            val nit = etNit.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val rolSeleccionado = autoCompleteRol.text.toString()

            // Validaciones críticas de campos obligatorios
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty() || rolSeleccionado.isEmpty()) {
                Toast.makeText(context, "Por favor completa Nombre, Apellido, Rol, Correo y Contraseña", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (dui.isNotEmpty() && dui.length < 10) {
                Toast.makeText(context, "Formato de DUI incompleto (00000000-0)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 💥 MAPEO AUTOMÁTICO: Jalamos el ID real correspondiente al texto seleccionado del mapa
            val idRol = mapaRoles[rolSeleccionado]
            if (idRol == null) {
                Toast.makeText(context, "Rol seleccionado no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.openDatabase()
            var cursor: Cursor? = null

            try {
                // 1. Validación de seguridad: Comprobar que el correo no esté registrado ya
                val queryCheck = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ?"
                cursor = db.rawQuery(queryCheck, arrayOf(correo))

                if (cursor != null && cursor.count > 0) {
                    Toast.makeText(context, "El correo electrónico ya se encuentra registrado", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // 2. Insertamos el nuevo personal en la BD con el ID_ROL automático
                val valores = ContentValues().apply {
                    put("ID_ROL", idRol)
                    put("NOMBRE_USUARIO", nombre)
                    put("APELLIDO_USUARIO", apellido)
                    put("CORREO_ELECTRONICO", correo)
                    put("CONTRASENA", password)
                    put("DUI_USUARIO", dui)
                    put("NIT_USUARIO", nit)
                    put("FECHA_CONTRATACION", fecha)
                    put("TELEFONO_USUARIO", telefono)
                    put("ESTADO", "Activo")
                }

                val resultado = db.insert("USUARIO", null, valores)

                if (resultado != -1L) {
                    Toast.makeText(context, "¡Personal creado con éxito! Ya puede iniciar sesión", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Error interno al insertar en SQLite", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                cursor?.close()
                db.close()
            }
        }
    }

    // 🔍 SELECCIÓN DE ROLES REALES PARA EL COMBOBOX
    private fun obtenerRolesDesdeBD() {
        listaNombresRoles.clear()
        mapaRoles.clear()

        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            val query = "SELECT ID_ROL, NOMBRE_ROL FROM ROL ORDER BY NOMBRE_ROL ASC"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL")) ?: ""

                    // Llenamos las colecciones dinámicas
                    listaNombresRoles.add(nombre)
                    mapaRoles[nombre] = id // "Administrador" -> 1, "Operativo" -> 2...
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al cargar catálogo de roles: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}