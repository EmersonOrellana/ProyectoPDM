package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView

class RolesAdapter(
    private var listaRoles: List<RolModel>,
    private val onAccionClick: (RolModel, String) -> Unit
) : RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreRolItem)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionRolItem)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditarRolItem)
        val btnEliminar: MaterialButton = view.findViewById(R.id.btnEliminarRolItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rol, parent, false)
        return RolesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = listaRoles[position]
        holder.tvNombre.text = rol.nombreRol
        holder.tvDescripcion.text = rol.descripcionRol

        holder.btnEditar.setOnClickListener { onAccionClick(rol, "EDITAR") }
        holder.btnEliminar.setOnClickListener { onAccionClick(rol, "ELIMINAR") }
    }

    override fun getItemCount(): Int = listaRoles.size

    fun filtrarLista(nuevaLista: List<RolModel>) {
        listaRoles = nuevaLista
        notifyDataSetChanged()
    }
}