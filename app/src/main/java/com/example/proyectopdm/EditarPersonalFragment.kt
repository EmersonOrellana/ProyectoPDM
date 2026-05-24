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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class EditarPersonalFragment : Fragment(R.layout.fragment_editar_personal) {

    private lateinit var dbHelper: DatabaseHelper
    private var correoEmpleadoAEditar: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Capturamos el correo enviado desde la lista del RecyclerView
        correoEmpleadoAEditar = arguments?.getString("correo_usuario")

        // 2. Vinculamos de forma exacta tus IDs del XML
        val etNombreCompleto = view.findViewById<EditText>(R.id.etEditarNombre)
        val etDui = view.findViewById<EditText>(R.id.etEditarDUI)
        val etNit = view.findViewById<EditText>(R.id.etEditarNIT)
        val etFecha = view.findViewById<EditText>(R.id.etEditarFecha)
        val etTelefono = view.findViewById<EditText>(R.id.etEditarTelefono)
        val etCorreo = view.findViewById<EditText>(R.id.etEditarCorreo)
        val etPassword = view.findViewById<EditText>(R.id.etEditarPassword)
        val autoCompleteRol = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditarRol)

        // Configuración de los roles en el desplegable
        val roles = listOf("Administrador", "Usuario Operativo")
        val adapterRoles = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        autoCompleteRol.setAdapter(adapterRoles)

        // 🗓️ CALENDARIO AUTOMÁTICO EN LA FECHA DE CONTRATACIÓN
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

        // 💥 MÁSCARA AUTOMÁTICA EN TIEMPO REAL - DUI
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

        // 💥 MÁSCARA AUTOMÁTICA EN TIEMPO REAL - NIT
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

        // 3. CARGAR LOS DATOS ACTUALES DESDE SQLITE
        if (!correoEmpleadoAEditar.isNullOrEmpty()) {
            cargarDatosEmpleado(correoEmpleadoAEditar!!, etNombreCompleto, etDui, etNit, etFecha, etTelefono, etCorreo, etPassword, autoCompleteRol)
        } else {
            Toast.makeText(context, "Error: No se recibió credencial de edición", Toast.LENGTH_SHORT).show()
        }

        // 4. LÓGICA DEL BOTÓN GUARDAR CAMBIOS (UPDATE)
        view.findViewById<Button>(R.id.btnGuardarCambios).setOnClickListener {
            val nombreCompletoTxt = etNombreCompleto.text.toString().trim()
            val duiTxt = etDui.text.toString().trim()
            val nitTxt = etNit.text.toString().trim()
            val fechaTxt = etFecha.text.toString().trim()
            val telefonoTxt = etTelefono.text.toString().trim()
            val correoTxt = etCorreo.text.toString().trim()
            val passwordTxt = etPassword.text.toString().trim()
            val rolSeleccionado = autoCompleteRol.text.toString()

            // Validaciones de seguridad de campos críticos
            if (nombreCompletoTxt.isEmpty() || correoTxt.isEmpty() || passwordTxt.isEmpty() || rolSeleccionado.isEmpty()) {
                Toast.makeText(context, "Nombre, Rol, Correo y Contraseña son campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (duiTxt.isNotEmpty() && duiTxt.length < 10) {
                Toast.makeText(context, "DUI incompleto (Formato: 00000000-0)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Separación del Nombre Completo en Nombre y Apellido para la base de datos
            val partesNombre = nombreCompletoTxt.split(" ", limit = 2)
            val nombreParaBD = partesNombre.getOrElse(0) { "" }
            val apellidoParaBD = partesNombre.getOrElse(1) { "" }

            val idRolMapeado = if (rolSeleccionado == "Administrador") 1 else 2

            val db = dbHelper.openDatabase()
            try {
                val valores = ContentValues().apply {
                    put("ID_ROL", idRolMapeado)
                    put("NOMBRE_USUARIO", nombreParaBD)
                    put("APELLIDO_USUARIO", apellidoParaBD)
                    put("DUI_USUARIO", duiTxt)
                    put("NIT_USUARIO", nitTxt)
                    put("FECHA_CONTRATACION", fechaTxt)
                    put("TELEFONO_USUARIO", telefonoTxt)
                    put("CORREO_ELECTRONICO", correoTxt)
                    put("CONTRASENA", passwordTxt)
                }

                // Hacemos el UPDATE basándonos en el correo original como llave de búsqueda
                val filasAfectadas = db.update(
                    "USUARIO",
                    valores,
                    "CORREO_ELECTRONICO = ?",
                    arrayOf(correoEmpleadoAEditar)
                )

                if (filasAfectadas > 0) {
                    Toast.makeText(context, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "No se pudo actualizar el registro", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }

        // Botón Cancelar
        view.findViewById<Button>(R.id.btnCancelarEdicion).setOnClickListener {
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
            parentFragmentManager.popBackStack()
        }
    }

    // 🔍 CONSULTA SELECT PARA RELLENAR EL FORMULARIO
    private fun cargarDatosEmpleado(
        correo: String,
        etNombreCompleto: EditText,
        etDui: EditText,
        etNit: EditText,
        etFecha: EditText,
        etTelefono: EditText,
        etCorreo: EditText,
        etPassword: EditText,
        autoCompleteRol: AutoCompleteTextView
    ) {
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            val query = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ?"
            cursor = db.rawQuery(query, arrayOf(correo))

            if (cursor != null && cursor.moveToFirst()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")) ?: ""
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO_USUARIO")) ?: ""

                // Unimos nombre y apellido para tu caja unificada "Nombre Completo"
                etNombreCompleto.setText("$nombre $apellido".trim())

                etCorreo.setText(cursor.getString(cursor.getColumnIndexOrThrow("CORREO_ELECTRONICO")) ?: "")
                etPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("CONTRASENA")) ?: "")
                etDui.setText(cursor.getString(cursor.getColumnIndexOrThrow("DUI_USUARIO")) ?: "")
                etNit.setText(cursor.getString(cursor.getColumnIndexOrThrow("NIT_USUARIO")) ?: "")
                etFecha.setText(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CONTRATACION")) ?: "")
                etTelefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_USUARIO")) ?: "")

                val idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                val nombreRol = if (idRol == 1) "Administrador" else "Usuario Operativo"
                autoCompleteRol.setText(nombreRol, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}