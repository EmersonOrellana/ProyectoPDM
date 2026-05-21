package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UnidadesMedidaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidades_medida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. BOTÓN FLOTANTE (+) -> AGREGAR
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_unidad)
        fabAgregar.setOnClickListener {
            val dialog = UnidadMedidaDialog(esEdicion = false)
            dialog.show(parentFragmentManager, "UnidadMedidaDialog")
        }

        // 2. BOTÓN LÁPIZ -> EDITAR (En la tarjeta de Kilogramos)
        val btnEditar = view.findViewById<ImageView>(R.id.btn_editar_item)
        btnEditar.setOnClickListener {
            val dialog = UnidadMedidaDialog(esEdicion = true)
            dialog.show(parentFragmentManager, "UnidadMedidaDialog_Edit")
        }

        // BOTÓN BASURERO -> ELIMINAR
        val btnEliminar = view.findViewById<ImageView>(R.id.btn_eliminar_item)
        btnEliminar.setOnClickListener {

            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_eliminar, null)

            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
            val alertDialog = builder.create()

            alertDialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))

            val btnCancelar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancelar_eliminar)
            val btnConfirmar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_confirmar_eliminar)

            btnCancelar.setOnClickListener {
                alertDialog.dismiss() // Solo cierra el cuadro
            }

            btnConfirmar.setOnClickListener {
                Toast.makeText(context, "Unidad eliminada correctamente", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
            
            alertDialog.show()
        }
    }
}