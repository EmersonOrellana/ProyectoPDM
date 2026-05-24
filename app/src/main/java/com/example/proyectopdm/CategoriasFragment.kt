package com.example.proyectopdm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriasFragment : Fragment(), OnCategoriaActualizadaListener {

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

        // 1. CONECTAR BUSCADOR
        val etBuscar = view.findViewById<EditText>(R.id.et_buscar_categorias)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    cargarLista() // Si borra todo, muestra todo
                } else {
                    val listaFiltrada = dbHelper.buscarCategorias(query)
                    adapter.actualizarLista(listaFiltrada) // Filtra al instante
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        cargarLista()

        view.findViewById<FloatingActionButton>(R.id.fab_agregar_categoria).setOnClickListener {
            val dialog = CategoriaDialog(esEdicion = false, listener = this)
            dialog.show(parentFragmentManager, "CategoriaDialog")
        }
    }

    // 2. Método llamado por el listener del Dialog
    override fun onCategoriaActualizada() {
        cargarLista()
    }

    private fun cargarLista() {
        val lista = dbHelper.obtenerCategorias()
        adapter = CategoriaAdapter(lista,
            onEditarClick = { categoria ->
                val dialog = CategoriaDialog(esEdicion = true, categoriaParaEditar = categoria, listener = this)
                dialog.show(parentFragmentManager, "CategoriaDialog_Edit")
            },
            onEliminarClick = { categoria ->
                mostrarDialogoEliminar(categoria)
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
            if (dbHelper.eliminarCategoria(categoria.idCategoria)) {
                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                cargarLista()
            } else {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}