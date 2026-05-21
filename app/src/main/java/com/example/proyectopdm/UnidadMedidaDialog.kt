package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager // Importación nueva
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton

class UnidadMedidaDialog(private val esEdicion: Boolean = false) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hacemos que el fondo del Dialog sea transparente para los bordes redondeados
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_unidad_medida, container, false)
    }

    // --- TRUCO MÁGICO AQUÍ PARA QUE NO SE VEA APLASTADO ---
    override fun onStart() {
        super.onStart()
        // Obligamos al diálogo a ocupar el 90% del ancho de la pantalla (dando márgenes)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_unidad)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_unidad)

        if (esEdicion) {
            tvTitulo.text = "Editar Unidad de Medida"
            btnConfirmar.text = "Guardar Cambios"
        }

        btnConfirmar.setOnClickListener {
            val accion = if (esEdicion) "actualizada" else "registrada"
            Toast.makeText(context, "Unidad $accion exitosamente", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }
}