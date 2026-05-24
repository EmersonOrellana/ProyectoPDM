package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoriaAdapter(
    private var lista: List<Categoria>, // 'var' es correcto para permitir filtros
    private val onEditarClick: (Categoria) -> Unit,
    private val onEliminarClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tv_item_nombre)
        val tvCodigo: TextView = view.findViewById(R.id.tv_item_codigo)
        val tvDescripcion: TextView = view.findViewById(R.id.tv_item_descripcion)
        val btnEditar: ImageView = view.findViewById(R.id.btn_editar_categoria)
        val btnEliminar: ImageView = view.findViewById(R.id.btn_eliminar_categoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        // Obtenemos el objeto de la posición actual
        val categoria = lista[position]

        holder.tvNombre.text = categoria.nombreCategoria
        holder.tvCodigo.text = categoria.codigoCategoria
        holder.tvDescripcion.text = categoria.descripcion

        // Usamos setOnClickListener asegurándonos de que cada evento sabe qué objeto editar/borrar
        holder.btnEditar.setOnClickListener {
            onEditarClick(categoria)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(categoria)
        }
    }

    override fun getItemCount(): Int = lista.size

    // Método vital para el buscador que acabamos de implementar
    fun actualizarLista(nuevaLista: List<Categoria>) {
        this.lista = nuevaLista
        notifyDataSetChanged() // Notifica al RecyclerView que los datos cambiaron
    }
}