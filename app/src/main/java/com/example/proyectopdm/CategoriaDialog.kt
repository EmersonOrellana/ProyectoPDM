package com.example.proyectopdm

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

interface OnCategoriaActualizadaListener {
    fun onCategoriaActualizada()
}

class CategoriaDialog(
    private val esEdicion: Boolean = false,
    private val categoriaParaEditar: Categoria? = null,
    private val listener: OnCategoriaActualizadaListener? = null
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_categoria, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<EditText>(R.id.et_nombre_cat)
        val etCodigo = view.findViewById<EditText>(R.id.et_codigo_cat)
        val etDescripcion = view.findViewById<EditText>(R.id.et_descripcion_cat)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_cat)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_cat) // <-- Asegúrate de que este ID sea el correcto en tu XML

        if (esEdicion && categoriaParaEditar != null) {
            view.findViewById<TextView>(R.id.tv_titulo_dialog_cat).text = "Editar Categoría"
            etNombre.setText(categoriaParaEditar.nombreCategoria)
            etCodigo.setText(categoriaParaEditar.codigoCategoria)
            etDescripcion.setText(categoriaParaEditar.descripcion)
        }

        // --- LÓGICA DEL BOTÓN CANCELAR ---
        btnCancelar.setOnClickListener {
            dismiss() // Cierra el diálogo inmediatamente sin hacer nada más
        }

        btnConfirmar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim()
            val desc = etDescripcion.text.toString().trim()

            if (nombre.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(context, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DatabaseHelper(requireContext())
            val db = dbHelper.openDatabase()

            try {
                if (esEdicion && categoriaParaEditar != null) {
                    db.execSQL(
                        "UPDATE CATEGORIA SET NOMBRE_CATEGORIA = ?, CODIGO_CATEGORIA = ?, DESCRIPCION = ? WHERE ID_CATEGORIA = ?",
                        arrayOf(nombre, codigo, desc, categoriaParaEditar.idCategoria.toString())
                    )
                    Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    db.execSQL(
                        "INSERT INTO CATEGORIA (NOMBRE_CATEGORIA, CODIGO_CATEGORIA, DESCRIPCION) VALUES (?, ?, ?)",
                        arrayOf(nombre, codigo, desc)
                    )
                    Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_SHORT).show()
                }

                listener?.onCategoriaActualizada()

            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
                dismiss() // Cierra el diálogo tras confirmar
            }
        }
    }
}