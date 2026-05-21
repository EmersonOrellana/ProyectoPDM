package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton

class ReglaConversionDialog(private val esEdicion: Boolean = false) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_regla_conversion, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog_regla)
        val spinnerOrigen = view.findViewById<Spinner>(R.id.spinner_origen)
        val spinnerDestino = view.findViewById<Spinner>(R.id.spinner_destino)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_regla)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_regla)

        // 1. Llenamos los menús desplegables con datos de prueba
        val opcionesUnidades = arrayOf("Seleccionar...", "Kilogramos", "Libras", "Quintales", "Sacos")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcionesUnidades)
        spinnerOrigen.adapter = adapter
        spinnerDestino.adapter = adapter

        // 2. Lógica de edición
        if (esEdicion) {
            tvTitulo.text = "Editar Regla de Conversión"
        }

        // 3. Botones
        btnConfirmar.setOnClickListener {
            val accion = if (esEdicion) "actualizada" else "registrada"
            Toast.makeText(context, "Regla $accion exitosamente", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }
}