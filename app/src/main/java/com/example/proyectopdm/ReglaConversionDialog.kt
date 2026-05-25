package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton

class ReglaConversionDialog(
    private val reglaAEditar: ReglaConversion? = null,
    private val onGuardado: () -> Unit
) : DialogFragment() {

    // Lista temporal para guardar las unidades consultadas de la BD
    private var listaUnidades: List<UnidadMedida> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_regla_conversion, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog_regla)
        val spinnerOrigen = view.findViewById<Spinner>(R.id.spinner_origen)
        val spinnerDestino = view.findViewById<Spinner>(R.id.spinner_destino)
        val etFactor = view.findViewById<EditText>(R.id.et_factor_regla)
        val etDescripcion = view.findViewById<EditText>(R.id.et_descripcion_regla)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_regla)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_regla)

        // 1. CARGAMOS LAS UNIDADES PARA LOS SPINNERS
        val repoUnidades = UnidadMedidaRepository(requireContext())
        listaUnidades = repoUnidades.obtenerUnidades()

        if (listaUnidades.isEmpty()) {
            Toast.makeText(context, "Primero debes registrar Unidades de Medida", Toast.LENGTH_LONG).show()
            dismiss()
            return
        }

        // Extraemos solo los nombres para mostrarlos en el menú
        val nombresUnidades = listaUnidades.map { "${it.nombreUnidad} (${it.abreviatura})" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresUnidades)
        spinnerOrigen.adapter = adapter
        spinnerDestino.adapter = adapter

        // 2. CONFIGURACIÓN SI ES EDICIÓN
        if (reglaAEditar != null) {
            tvTitulo.text = "Editar Regla de Conversión"
            btnConfirmar.text = "Guardar Cambios"
            etFactor.setText(reglaAEditar.factorConversion.toString())
            etDescripcion.setText(reglaAEditar.descripcionConversion)

            // Buscamos en qué posición del Spinner están las unidades guardadas previamente
            val posOrigen = listaUnidades.indexOfFirst { it.idUnidad == reglaAEditar.idUnidadOrigen }
            val posDestino = listaUnidades.indexOfFirst { it.idUnidad == reglaAEditar.idUnidadDestino }
            if (posOrigen >= 0) spinnerOrigen.setSelection(posOrigen)
            if (posDestino >= 0) spinnerDestino.setSelection(posDestino)
        }

        // 3. GUARDAR REGLA
        val repoReglas = ReglaConversionRepository(requireContext())

        btnConfirmar.setOnClickListener {
            val factorTexto = etFactor.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val posOrigen = spinnerOrigen.selectedItemPosition
            val posDestino = spinnerDestino.selectedItemPosition

            if (factorTexto.isNotEmpty() && posOrigen >= 0 && posDestino >= 0) {
                // Prevenimos que origen y destino sean iguales (Ej: 1 Saco = 5 Sacos)
                if (posOrigen == posDestino) {
                    Toast.makeText(context, "El origen y destino no pueden ser iguales", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val factor = factorTexto.toDoubleOrNull()
                if (factor == null || factor <= 0) {
                    Toast.makeText(context, "El factor debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Obtenemos los IDs reales de la base de datos
                val idOrigen = listaUnidades[posOrigen].idUnidad
                val idDestino = listaUnidades[posDestino].idUnidad

                val regla = ReglaConversion(
                    idConversion = reglaAEditar?.idConversion ?: 0,
                    idUnidadOrigen = idOrigen,
                    idUnidadDestino = idDestino,
                    factorConversion = factor,
                    descripcionConversion = if (descripcion.isEmpty()) "Conversión estándar" else descripcion
                )

                val exito = if (reglaAEditar == null) {
                    repoReglas.insertarRegla(regla) != -1L
                } else {
                    repoReglas.actualizarRegla(regla)
                }

                if (exito) {
                    Toast.makeText(context, "Regla guardada correctamente", Toast.LENGTH_SHORT).show()
                    onGuardado()
                    dismiss()
                } else {
                    Toast.makeText(context, "Error al guardar la regla", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener { dismiss() }
    }
}