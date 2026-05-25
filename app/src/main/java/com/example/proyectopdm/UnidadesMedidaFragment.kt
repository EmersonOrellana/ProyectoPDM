package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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
    private lateinit var etBuscar: EditText // Declaramos el EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_unidad)
        contenedorUnidades = view.findViewById(R.id.llContenedorUnidades)
        etBuscar = view.findViewById(R.id.et_buscar_unidades) // Lo vinculamos al ID que acabamos de crear

        // Cargar datos al iniciar (sin ningún filtro)
        cargarDatos("")

        //  BUSCADOR EN TIEMPO REAL
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtramos la lista cada vez que se escribe una letra
                cargarDatos(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Botón flotante para Agregar
        fabAgregar.setOnClickListener {
            val dialog = UnidadMedidaDialog(unidadAEditar = null) {

                etBuscar.setText("")
                cargarDatos("")
            }
            dialog.show(parentFragmentManager, "UnidadMedidaDialog_Add")
        }
    }


    fun cargarDatos(filtro: String) {
        contenedorUnidades.removeAllViews()
        val repo = UnidadMedidaRepository(requireContext())
        val lista = repo.obtenerUnidades()

        for (unidad in lista) {

            // --- LÓGICA DE FILTRADO ---

            val textoBusqueda = "${unidad.nombreUnidad} ${unidad.abreviatura} ${unidad.descripcionUso}".lowercase()


            if (filtro.isNotEmpty() && !textoBusqueda.contains(filtro.lowercase())) {
                continue
            }

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
                    // Refrescamos manteniendo el texto actual del buscador
                    cargarDatos(etBuscar.text.toString())
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

                cargarDatos(etBuscar.text.toString())
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}