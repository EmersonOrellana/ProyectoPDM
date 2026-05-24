package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriasFragment : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var adapter: CategoriaAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        rvCategorias = view.findViewById(R.id.rv_categorias)
        rvCategorias.layoutManager = LinearLayoutManager(requireContext())

        // Cargar los datos desde SQLite
        cargarLista()

        // BOTÓN FLOTANTE (+) -> AGREGAR
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_categoria)
        fabAgregar.setOnClickListener {
            val dialog = CategoriaDialog(esEdicion = false)
            dialog.show(parentFragmentManager, "CategoriaDialog")
        }
    }

    // Esta función se ejecuta siempre que regreses a esta pantalla
    override fun onResume() {
        super.onResume()
        cargarLista() // Refresca la lista si agregaste algo nuevo
    }

    private fun cargarLista() {
        val lista = dbHelper.obtenerCategorias()

        adapter = CategoriaAdapter(lista,
            onEditarClick = { categoriaSeleccionada ->
                // Acción de Editar
                val dialog = CategoriaDialog(esEdicion = true)
                dialog.show(parentFragmentManager, "CategoriaDialog_Edit")
            },
            onEliminarClick = { categoriaSeleccionada ->
                // Tu Acción de Eliminar (Diálogo personalizado)
                mostrarDialogoEliminar(categoriaSeleccionada)
            }
        )
        rvCategorias.adapter = adapter
    }

    private fun mostrarDialogoEliminar(categoria: Categoria) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_eliminar, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))

        val btnCancelar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancelar_eliminar)
        val btnConfirmar = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_confirmar_eliminar)

        btnCancelar.setOnClickListener { alertDialog.dismiss() }
        btnConfirmar.setOnClickListener {
            // Aquí iría tu lógica db.delete(...) más adelante
            Toast.makeText(context, "Categoría ${categoria.nombreCategoria} eliminada", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}