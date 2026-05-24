package com.example.proyectopdm

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
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

        // Referencias a los componentes del XML
        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog_cat)
        val etNombre = view.findViewById<EditText>(R.id.et_nombre_cat)
        val etCodigo = view.findViewById<EditText>(R.id.et_codigo_cat)
        val etDescripcion = view.findViewById<EditText>(R.id.et_descripcion_cat)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_cat)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_cat)

        if (esEdicion) {
            tvTitulo.text = "Editar Categoría"
        }

        btnConfirmar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim()
            val desc = etDescripcion.text.toString().trim()

            if (nombre.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(context, "Nombre y Código son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lógica de inserción en Base de Datos
            val dbHelper = DatabaseHelper(requireContext())
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("NOMBRE_CATEGORIA", nombre)
                put("CODIGO_CATEGORIA", codigo)
                put("DESCRIPCION", desc)
            }

            val resultado = db.insert("CATEGORIA", null, values)

            if (resultado != -1L) {
                val accion = if (esEdicion) "actualizada" else "registrada"
                Toast.makeText(context, "Categoría $accion exitosamente", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }
}