package com.example.proyectopdm

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class RegistrarTransportistaFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrar_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Referenciar los campos del XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreCompleto)
        val etDui = view.findViewById<EditText>(R.id.etDui)
        val etNit = view.findViewById<EditText>(R.id.etNit)
        val etPlaca = view.findViewById<EditText>(R.id.etPlaca)
        val etLicencia = view.findViewById<EditText>(R.id.etLicencia)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefono)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreoElectronico)
        val spinnerLicencia = view.findViewById<Spinner>(R.id.spTipoLicencia)

        // 2. Aplicar límites de caracteres máximos (contando guiones)
        etDui.filters = arrayOf(InputFilter.LengthFilter(10)) // 00000000-0 (10 caracteres)
        etNit.filters = arrayOf(InputFilter.LengthFilter(10)) // 00000000-0 (Igual al DUI según tu diseño)

        // 3. Formateador automático con Guion para el DUI
        etDui.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val str = s.toString().replace("-", "")
                if (str.length >= 9) {
                    val formatted = str.substring(0, 8) + "-" + str.substring(8)
                    etDui.setText(formatted)
                    etDui.setSelection(formatted.length)
                }
                isUpdating = false
            }
        })

        // 4. Formateador automático con Guion para el NIT
        etNit.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val str = s.toString().replace("-", "")
                if (str.length >= 9) {
                    val formatted = str.substring(0, 8) + "-" + str.substring(8)
                    etNit.setText(formatted)
                    etNit.setSelection(formatted.length)
                }
                isUpdating = false
            }
        })

        // 5. Configurar el Spinner de Tipo de Licencia
        val opcionesLicencia = arrayOf("Pesada", "Pesada T", "Liviana", "Particular")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesLicencia)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLicencia.adapter = adapter

        // 6. Configurar botón Cancelar
        view.findViewById<Button>(R.id.btnCancelarRegistro).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 7. Configurar botón Crear Usuario (Guardar)
        view.findViewById<Button>(R.id.btnCrearUsuario).setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val dui = etDui.text.toString().trim()
            val nit = etNit.text.toString().trim()
            val placa = etPlaca.text.toString().trim()
            val licencia = etLicencia.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val tipoLicencia = spinnerLicencia.selectedItem.toString()

            // Validaciones de campos obligatorios básicos
            if (nombre.isEmpty() || placa.isEmpty()) {
                Toast.makeText(context, "Por favor, llena los campos obligatorios (Nombre y Placa)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar longitud del DUI (Debe tener 10 caracteres incluyendo el guion: 00000000-0)
            if (dui.isNotEmpty() && dui.length < 10) {
                Toast.makeText(context, "El DUI debe tener el formato correcto (8 dígitos - 1 dígito)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar longitud del NIT (Debe tener 10 caracteres incluyendo el guion: 00000000-0)
            if (nit.isNotEmpty() && nit.length < 10) {
                Toast.makeText(context, "El NIT debe tener el formato correcto (8 dígitos - 1 dígito)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val db = dbHelper.openDatabase()

                // Mapeo EXACTO con las columnas reales de la tabla TRANSPORTISTA
                val valores = ContentValues().apply {
                    put("NOMBRE_TRANSPORTISTA", nombre)
                    put("DUI_TRANSPORTISTA", dui)
                    put("NIT_TRANSPORTISTA", nit)
                    put("PLACA_TRANSPORTISTA", placa)
                    put("NO_LICENCIA", licencia)
                    put("TIPO_LICENCIA", tipoLicencia)
                    put("TELEFONO_TRANSPORTISTA", telefono)
                    put("CORREO_TRANSPORTISTA", correo)
                }

                val resultado = db.insert("TRANSPORTISTA", null, valores)
                db.close()

                if (resultado != -1L) {
                    Toast.makeText(context, "Transportista registrado con éxito", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}