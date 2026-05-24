package com.example.proyectopdm

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class EditarTransportistaFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private var transportistaAEditar: Transportista? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editar_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Base de Datos y recuperar el objeto seleccionado
        dbHelper = DatabaseHelper(requireContext())
        transportistaAEditar = arguments?.getSerializable("transportista") as? Transportista

        // 1. Referenciar las vistas exactas de tu XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreEditar)
        val etDui = view.findViewById<EditText>(R.id.etDuiEditar)
        val etNit = view.findViewById<EditText>(R.id.etNitEditar)
        val etPlaca = view.findViewById<EditText>(R.id.etPlacaEditar)
        val etLicencia = view.findViewById<EditText>(R.id.etLicenciaEditar)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefonoEditar)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreoEditar)
        val spinnerLicencia = view.findViewById<Spinner>(R.id.spTipoLicenciaEditar)

        // Limits y formateadores con guion automático para DUI y NIT (Máximo 10 caracteres)
        etDui.filters = arrayOf(InputFilter.LengthFilter(10))
        etNit.filters = arrayOf(InputFilter.LengthFilter(10))
        setupGuionWatcher(etDui)
        setupGuionWatcher(etNit)

        // Configurar las opciones del Spinner
        val opciones = arrayOf("Pesada", "Pesada T", "Liviana", "Particular")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLicencia.adapter = adapter

        // 2. Cargar los datos del transportista en el formulario si viene de la lista
        transportistaAEditar?.let { motorista ->
            etNombre.setText(motorista.nombre)
            etDui.setText(motorista.dui)
            etNit.setText(motorista.nit)
            etPlaca.setText(motorista.placa)
            etLicencia.setText(motorista.licencia)
            etTelefono.setText(motorista.telefono)
            etCorreo.setText(motorista.correo)

            // Seleccionar el tipo de licencia correcto en el Spinner
            val posicion = opciones.indexOf(motorista.tipoLicencia)
            if (posicion >= 0) spinnerLicencia.setSelection(posicion)
        }

        // 3. Botón Cancelar
        view.findViewById<Button>(R.id.btnCancelarEditar).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 4. Botón Actualizar (Lógica real de la BD)
        view.findViewById<Button>(R.id.btnActualizarUsuario).setOnClickListener {
            val idTransportista = transportistaAEditar?.id ?: return@setOnClickListener

            val nombre = etNombre.text.toString().trim()
            val dui = etDui.text.toString().trim()
            val nit = etNit.text.toString().trim()
            val placa = etPlaca.text.toString().trim()
            val licencia = etLicencia.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val tipoLicencia = spinnerLicencia.selectedItem.toString()

            // Validaciones rápidas obligatorias
            if (nombre.isEmpty() || placa.isEmpty()) {
                Toast.makeText(context, "El nombre y la placa son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dui.isNotEmpty() && dui.length < 10) {
                Toast.makeText(context, "El DUI debe estar completo (00000000-0)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val db = dbHelper.openDatabase()

                // Mapeo exacto con las columnas de tu SQLite
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

                // Ejecutar el UPDATE usando el ID único de la fila
                val filasAfectadas = db.update(
                    "TRANSPORTISTA",
                    valores,
                    "ID_TRANSPORTISTA = ?",
                    arrayOf(idTransportista.toString())
                )
                db.close()

                if (filasAfectadas > 0) {
                    Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack() // Regresa al listado principal
                } else {
                    Toast.makeText(context, "No se guardó ningún cambio", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Formateador automático de guiones para DUI y NIT al escribir
    private fun setupGuionWatcher(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val str = s.toString().replace("-", "")
                if (str.length >= 9) {
                    val formatted = str.substring(0, 8) + "-" + str.substring(8)
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                }
                isUpdating = false
            }
        })
    }
}