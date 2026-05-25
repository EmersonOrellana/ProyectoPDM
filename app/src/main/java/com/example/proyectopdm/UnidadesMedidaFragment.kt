package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UnidadesMedidaFragment : Fragment(R.layout.fragment_unidades_medida) {

    private lateinit var contenedorUnidades: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_unidad)
        contenedorUnidades = view.findViewById(R.id.llContenedorUnidades)

        // Cargar datos al iniciar
        cargarDatos()

        // Botón flotante para Agregar
        fabAgregar.setOnClickListener {
            // Pasamos null porque es una unidad nueva
            val dialog = UnidadMedidaDialog(unidadAEditar = null) {
                cargarDatos() // Se ejecuta al guardar
            }
            dialog.show(parentFragmentManager, "UnidadMedidaDialog_Add")
        }
    }

    fun cargarDatos() {
        contenedorUnidades.removeAllViews()
        val repo = UnidadMedidaRepository(requireContext())
        val lista = repo.obtenerUnidades()

        for (unidad in lista) {
            val vistaTarjeta = LayoutInflater.from(context).inflate(R.layout.item_unidad, contenedorUnidades, false)

            val tvNombre = vistaTarjeta.findViewById<TextView>(R.id.tvNombreUnidad)
            val tvDesc = vistaTarjeta.findViewById<TextView>(R.id.tvDescUnidad)
            val btnEditar = vistaTarjeta.findViewById<ImageView>(R.id.btn_editar_item)
            val btnEliminar = vistaTarjeta.findViewById<ImageView>(R.id.btn_eliminar_item)

            tvNombre.text = "${unidad.nombreUnidad} (${unidad.abreviatura})"
            tvDesc.text = unidad.descripcionUso

            // --- BOTÓN EDITAR ---
            btnEditar.setOnClickListener {
                val dialog = UnidadMedidaDialog(unidadAEditar = unidad) {
                    cargarDatos() // Refresca la lista al cerrar el diálogo
                }
                dialog.show(parentFragmentManager, "UnidadMedidaDialog_Edit")
            }

            // --- BOTÓN ELIMINAR ---
            btnEliminar.setOnClickListener {
                mostrarDialogoEliminar(unidad, repo)
            }

            contenedorUnidades.addView(vistaTarjeta)
        }
    }

    private fun mostrarDialogoEliminar(unidad: UnidadMedida, repo: UnidadMedidaRepository) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_eliminar, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btn_cancelar_eliminar)
        val btnConfirmar = dialogView.findViewById<MaterialButton>(R.id.btn_confirmar_eliminar)

        btnCancelar.setOnClickListener { alertDialog.dismiss() }

        btnConfirmar.setOnClickListener {
            val eliminado = repo.eliminarUnidad(unidad.idUnidad)
            if (eliminado) {
                Toast.makeText(context, "Unidad eliminada", Toast.LENGTH_SHORT).show()
                cargarDatos()
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}