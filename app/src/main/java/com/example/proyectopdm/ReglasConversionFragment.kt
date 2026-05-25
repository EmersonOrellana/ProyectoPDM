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

class ReglasConversionFragment : Fragment(R.layout.fragment_reglas_conversion) {

    private lateinit var contenedorReglas: LinearLayout
    private lateinit var etBuscar: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contenedorReglas = view.findViewById(R.id.llContenedorReglas)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_regla)
        etBuscar = view.findViewById(R.id.et_buscar_reglas)

        // Carga inicial sin ningún filtro
        cargarDatos("")

        // 1. CONFIGURACIÓN DEL BUSCADOR EN TIEMPO REAL
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cada vez que se escribe o borra una letra, recargamos la lista con el filtro
                cargarDatos(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 2. CONFIGURACIÓN DEL BOTÓN AGREGAR
        fabAgregar.setOnClickListener {
            val dialog = ReglaConversionDialog(reglaAEditar = null) {
                // Limpiamos el buscador y recargamos todo al guardar una nueva regla
                etBuscar.setText("")
                cargarDatos("")
            }
            dialog.show(parentFragmentManager, "ReglaConversionDialog_Add")
        }
    }

    // Modificamos cargarDatos para recibir un parámetro de búsqueda
    fun cargarDatos(filtro: String) {
        contenedorReglas.removeAllViews()
        val repoReglas = ReglaConversionRepository(requireContext())
        val repoUnidades = UnidadMedidaRepository(requireContext())

        val listaReglas = repoReglas.obtenerReglas()
        val listaUnidades = repoUnidades.obtenerUnidades()

        for (regla in listaReglas) {
            // Buscamos los nombres de las unidades basándonos en los IDs
            val unidadOrigen = listaUnidades.find { it.idUnidad == regla.idUnidadOrigen }?.nombreUnidad ?: "Desconocido"
            val unidadDestino = listaUnidades.find { it.idUnidad == regla.idUnidadDestino }?.nombreUnidad ?: "Desconocido"
            val descripcion = regla.descripcionConversion

            // --- LÓGICA DE FILTRADO ---
            // Creamos una cadena con todos los textos por los que queremos buscar
            val textoBusqueda = "$unidadOrigen $unidadDestino $descripcion".lowercase()

            // Si el filtro no está vacío y el texto NO contiene lo que escribimos, saltamos esta tarjeta
            if (filtro.isNotEmpty() && !textoBusqueda.contains(filtro.lowercase())) {
                continue
            }

            // Si pasa el filtro, inflamos y mostramos la tarjeta
            val vistaTarjeta = LayoutInflater.from(context).inflate(R.layout.item_regla_conversion, contenedorReglas, false)

            val tvOrigen = vistaTarjeta.findViewById<TextView>(R.id.tv_origen_regla)
            val tvDestino = vistaTarjeta.findViewById<TextView>(R.id.tv_destino_regla)
            val btnEditar = vistaTarjeta.findViewById<ImageView>(R.id.btn_editar_regla)
            val btnEliminar = vistaTarjeta.findViewById<ImageView>(R.id.btn_eliminar_regla)

            tvOrigen.text = "1 $unidadOrigen ="
            tvDestino.text = "${regla.factorConversion} $unidadDestino"

            btnEditar.setOnClickListener {
                val dialog = ReglaConversionDialog(reglaAEditar = regla) {
                    cargarDatos(etBuscar.text.toString()) // Mantenemos el filtro actual al refrescar
                }
                dialog.show(parentFragmentManager, "ReglaConversionDialog_Edit")
            }

            btnEliminar.setOnClickListener {
                mostrarDialogoEliminar(regla, repoReglas)
            }

            contenedorReglas.addView(vistaTarjeta)
        }
    }

    private fun mostrarDialogoEliminar(regla: ReglaConversion, repo: ReglaConversionRepository) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_eliminar, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btn_cancelar_eliminar)
        val btnConfirmar = dialogView.findViewById<MaterialButton>(R.id.btn_confirmar_eliminar)

        btnCancelar.setOnClickListener { alertDialog.dismiss() }

        btnConfirmar.setOnClickListener {
            if (repo.eliminarRegla(regla.idConversion)) {
                Toast.makeText(context, "Regla eliminada", Toast.LENGTH_SHORT).show()
                // Recargamos manteniendo la búsqueda actual
                cargarDatos(etBuscar.text.toString())
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}