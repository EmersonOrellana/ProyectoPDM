package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MaterialAdapter(
    private val listaMateriales: List<Material>,
    private val onBajaClick: (Material) -> Unit,
    private val onEditarClick: (Material) -> Unit // Nuevo parámetro para editar
) : RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreMaterial)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaLabel)
        val tvUnidad: TextView = view.findViewById(R.id.tvUnidadLabel)
        val btnBaja: Button = view.findViewById(R.id.btnBajaMaterial)
        val btnEditar: Button = view.findViewById(R.id.btnEditarMaterial) // Vinculado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = listaMateriales[position]

        holder.tvNombre.text = material.nombre
        holder.tvCategoria.text = "Categoría: ${material.nombreCategoria}"
        holder.tvUnidad.text = "Unidad: ${material.nombreUnidad}"

        // Click para eliminar
        holder.btnBaja.setOnClickListener {
            onBajaClick(material)
        }

        // Click para editar
        holder.btnEditar.setOnClickListener {
            onEditarClick(material)
        }
    }

    override fun getItemCount() = listaMateriales.size
}