package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class MatrizAccesoAdapter(
    private var listaAccesos: List<AccesoModulo>
) : RecyclerView.Adapter<MatrizAccesoAdapter.MatrizViewHolder>() {

    class MatrizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreModulo: TextView = view.findViewById(R.id.tv_nombre_modulo)
        val switchVer: SwitchMaterial = view.findViewById(R.id.switch_ver)
        val switchEditar: SwitchMaterial = view.findViewById(R.id.switch_editar)
        val switchEliminar: SwitchMaterial = view.findViewById(R.id.switch_eliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatrizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matriz_fila, parent, false)
        return MatrizViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatrizViewHolder, position: Int) {
        val acceso = listaAccesos[position]

        holder.tvNombreModulo.text = acceso.nombreModulo

        // Apagamos los listeners momentáneamente para no disparar eventos falsos al hacer scroll
        holder.switchVer.setOnCheckedChangeListener(null)
        holder.switchEditar.setOnCheckedChangeListener(null)
        holder.switchEliminar.setOnCheckedChangeListener(null)

        // Asignamos los valores de la base de datos a los switches
        holder.switchVer.isChecked = acceso.puedeVer
        holder.switchEditar.isChecked = acceso.puedeEditar
        holder.switchEliminar.isChecked = acceso.puedeEliminar

        // Volvemos a encender los listeners para capturar lo que el usuario haga
        holder.switchVer.setOnCheckedChangeListener { _, isChecked ->
            acceso.puedeVer = isChecked
        }
        holder.switchEditar.setOnCheckedChangeListener { _, isChecked ->
            acceso.puedeEditar = isChecked
        }
        holder.switchEliminar.setOnCheckedChangeListener { _, isChecked ->
            acceso.puedeEliminar = isChecked
        }
    }

    override fun getItemCount(): Int = listaAccesos.size

    // Función para obtener la lista actualizada al momento de guardar
    fun obtenerDatosActualizados(): List<AccesoModulo> = listaAccesos
}