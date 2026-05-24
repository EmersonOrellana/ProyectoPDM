package com.example.proyectopdm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class TransportistaAdapter(
    private var listaOriginal: List<Transportista>,
    private val onVerFichaClick: (Transportista) -> Unit,
    private val onEditarClick: (Transportista) -> Unit,
    private val onBajaClick: (Transportista) -> Unit
) : RecyclerView.Adapter<TransportistaAdapter.TransportistaViewHolder>() {

    private var listaFiltrada: List<Transportista> = listaOriginal

    class TransportistaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreTrans)
        val tvPlaca: TextView = view.findViewById(R.id.tvPlacaTrans)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoTrans)
        val btnVerFicha: MaterialButton = view.findViewById(R.id.btnVerFicha)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditarTrans)
        val btnBaja: MaterialButton = view.findViewById(R.id.btnBajaTrans)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransportistaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transportista, parent, false)
        return TransportistaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransportistaViewHolder, position: Int) {
        val transportista = listaFiltrada[position]

        holder.tvNombre.text = transportista.nombre
        holder.tvPlaca.text = "Placa: ${transportista.placa}"
        holder.tvTelefono.text = "Tel: ${transportista.telefono}"

        // Delegamos los clics directos al fragmento padre
        holder.btnVerFicha.setOnClickListener { onVerFichaClick(transportista) }
        holder.btnEditar.setOnClickListener { onEditarClick(transportista) }
        holder.btnBaja.setOnClickListener { onBajaClick(transportista) }
    }

    override fun getItemCount(): Int = listaFiltrada.size

    // Filtro dinámico en tiempo real para el buscador
    fun filtrar(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombre.lowercase().contains(texto.lowercase()) ||
                        it.placa.lowercase().contains(texto.lowercase()) ||
                        it.telefono.contains(texto)
            }
        }
        notifyDataSetChanged()
    }

    fun actualizarLista(nuevaLista: List<Transportista>) {
        listaOriginal = nuevaLista
        listaFiltrada = nuevaLista
        notifyDataSetChanged()
    }
}