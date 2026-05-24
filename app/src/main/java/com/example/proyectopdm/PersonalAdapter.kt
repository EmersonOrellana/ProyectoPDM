package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView

class PersonalAdapter(
    private val listaPersonal: List<UsuarioModel>,
    private val onAccionClick: (UsuarioModel, String) -> Unit
) : RecyclerView.Adapter<PersonalAdapter.PersonalViewHolder>() {

    class PersonalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreCompleto)
        val tvNit: TextView = view.findViewById(R.id.tvNit)
        val tvRol: TextView = view.findViewById(R.id.tvRol)
        val btnFicha: MaterialButton = view.findViewById(R.id.btnFicha)
        val btnProyecto: MaterialButton = view.findViewById(R.id.btnProyecto)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditar)
        val btnBaja: MaterialButton = view.findViewById(R.id.btnBaja)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_personal, parent, false)
        return PersonalViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) {
        val emp = listaPersonal[position]

        // Mapeado directo a tus variables exactas en minúscula
        holder.tvNombre.text = "${emp.nombre} ${emp.apellido}"
        holder.tvNit.text = if (emp.nit.isEmpty()) "NIT: No asignado" else "NIT: ${emp.nit}"
        holder.tvRol.text = when(emp.idRol) {
            1 -> "Rol: Administrador"
            2 -> "Rol: Usuario Operativo"
            else -> "Rol: Personal"
        }

        holder.btnFicha.setOnClickListener { onAccionClick(emp, "FICHA") }
        holder.btnProyecto.setOnClickListener { onAccionClick(emp, "PROYECTO") }
        holder.btnEditar.setOnClickListener { onAccionClick(emp, "EDITAR") }
        holder.btnBaja.setOnClickListener { onAccionClick(emp, "BAJA") }
    }

    override fun getItemCount(): Int = listaPersonal.size
}