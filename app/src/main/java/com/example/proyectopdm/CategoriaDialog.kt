package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton

class CategoriaDialog(private val esEdicion: Boolean = false) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_categoria, container, false)
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

        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog_cat)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_cat)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_cat)

        if (esEdicion) {
            tvTitulo.text = "Editar Categoría"
        }

        btnConfirmar.setOnClickListener {
            val accion = if (esEdicion) "actualizada" else "registrada"
            Toast.makeText(context, "Categoría $accion exitosamente", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }
}