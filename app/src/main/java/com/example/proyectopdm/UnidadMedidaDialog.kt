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

class UnidadMedidaDialog(
    private val unidadAEditar: UnidadMedida? = null, // Si es null -> Nuevo, Si tiene datos -> Editar
    private val onGuardado: () -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_unidad_medida, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialog)
        val etNombre = view.findViewById<EditText>(R.id.et_nombre_unidad)
        val etAbreviatura = view.findViewById<EditText>(R.id.et_abreviatura_unidad)
        val etDescripcion = view.findViewById<EditText>(R.id.et_descripcion_unidad)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btn_confirmar_unidad)
        val btnCancelar = view.findViewById<MaterialButton>(R.id.btn_cancelar_unidad)
        val repo = UnidadMedidaRepository(requireContext())

        // Configuración inicial si es edición
        if (unidadAEditar != null) {
            tvTitulo.text = "Editar Unidad"
            btnConfirmar.text = "Guardar Cambios"
            etNombre.setText(unidadAEditar.nombreUnidad)
            etAbreviatura.setText(unidadAEditar.abreviatura)
            etDescripcion.setText(unidadAEditar.descripcionUso)
        }

        btnConfirmar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val abrev = etAbreviatura.text.toString().trim()
            val desc = etDescripcion.text.toString().trim()

            if (nombre.isNotEmpty() && abrev.isNotEmpty()) {

                // Si es edición, mantenemos el ID. Si es nuevo, el ID será 0 (autoincrementable en BD)
                val unidad = UnidadMedida(
                    idUnidad = unidadAEditar?.idUnidad ?: 0,
                    nombreUnidad = nombre,
                    abreviatura = abrev,
                    descripcionUso = desc
                )

                val exito = if (unidadAEditar == null) {
                    repo.insertarUnidad(unidad) != -1L
                } else {
                    repo.actualizarUnidad(unidad)
                }

                if (exito) {
                    Toast.makeText(context, "Operación exitosa", Toast.LENGTH_SHORT).show()
                    onGuardado()
                    dismiss()
                } else {
                    Toast.makeText(context, "Error en la base de datos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Nombre y Abreviatura son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener { dismiss() }
    }
}