package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contenedor = view.findViewById<LinearLayout>(R.id.contenedorProyectos)
        cargarProyectos(contenedor)
        configurarFABs(view)
    }

    private fun cargarProyectos(contenedor: LinearLayout) {
        val dbHelper = DatabaseHelper(requireContext())
        val listaProyectos = dbHelper.obtenerProyectos()

        contenedor.removeAllViews()
        val inflater = LayoutInflater.from(context)

        for (proyecto in listaProyectos) {
            val card = inflater.inflate(R.layout.item_proyecto, contenedor, false)

            // ASIGNACIÓN EXACTA:
            // 1. Título arriba (NOMBRE_PROYECTO)
            card.findViewById<TextView>(R.id.tvTitulo).text = proyecto.nombre

            // 2. Estado arriba derecha (ESTADO)
            card.findViewById<TextView>(R.id.tvEstado).text = proyecto.estado

            // 3. Fecha abajo (FECHA_INICIO)
            card.findViewById<TextView>(R.id.tvFecha).text = "Inicio: ${proyecto.fecha}"

            // Botones
            card.findViewById<Button>(R.id.btnDetalles).setOnClickListener {
                val fragmentoDetalle = DetalleProyectoFragment().apply {
                    arguments = Bundle().apply {
                        putString("nombre_proyecto", proyecto.nombre)
                        putInt("id_proyecto", proyecto.id)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragmentoDetalle)
                    .addToBackStack(null)
                    .commit()
            }

            card.findViewById<Button>(R.id.btnEditar).setOnClickListener {
                Toast.makeText(context, "Editar ${proyecto.nombre}", Toast.LENGTH_SHORT).show()
            }

            card.findViewById<Button>(R.id.btnEliminar).setOnClickListener {
                if (dbHelper.eliminarProyecto(proyecto.id)) {
                    Toast.makeText(context, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                    cargarProyectos(contenedor)
                }
            }

            contenedor.addView(card)
        }
    }

    private fun configurarFABs(view: View) {
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarProyecto)
        val fabAuto = view.findViewById<FloatingActionButton>(R.id.fabAuto)

        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, CrearProyectoFragment())
                .addToBackStack(null)
                .commit()
        }

        fabAuto.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, TransportistasFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}