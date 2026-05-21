package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReglasConversionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reglas_conversion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. BOTÓN FLOTANTE (+) -> AGREGAR
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_regla)
        fabAgregar.setOnClickListener {
            val dialog = ReglaConversionDialog(esEdicion = false)
            dialog.show(parentFragmentManager, "ReglaConversionDialog")
        }

        // 2. BOTÓN LÁPIZ -> EDITAR
        val btnEditar = view.findViewById<ImageView>(R.id.btn_editar_regla)
        btnEditar.setOnClickListener {
            val dialog = ReglaConversionDialog(esEdicion = true)
            dialog.show(parentFragmentManager, "ReglaConversionDialog_Edit")
        }

        // 3. BOTÓN BASURERO -> ELIMINAR
        val btnEliminar = view.findViewById<ImageView>(R.id.btn_eliminar_regla)
        btnEliminar.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_eliminar, null)
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
            val alertDialog = builder.create()

            alertDialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))

            val btnCancelar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancelar_eliminar)
            val btnConfirmar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_confirmar_eliminar)

            btnCancelar.setOnClickListener { alertDialog.dismiss() }
            btnConfirmar.setOnClickListener {
                Toast.makeText(context, "Regla de conversión eliminada", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}