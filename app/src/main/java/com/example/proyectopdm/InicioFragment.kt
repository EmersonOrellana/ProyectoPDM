package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InicioFragment : Fragment(R.layout.fragment_inicio) { // Asegúrate que el XML se llame así

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de los Proyectos X, Y, Z
        configurarProyecto(view, "Proyecto X", R.id.btnDetallesX, R.id.btnEditarX, R.id.btnEliminarX)
        configurarProyecto(view, "Proyecto Y", R.id.btnDetallesY, R.id.btnEditarY, R.id.btnEliminarY)
        configurarProyecto(view, "Proyecto Z", R.id.btnDetallesZ, R.id.btnEditarZ, R.id.btnEliminarZ)

        // Configuración de botones flotantes (FABs)
        configurarFABs(view)
    }

    private fun configurarProyecto(view: View, nombreProyecto: String, idDetalle: Int, idEditar: Int, idEliminar: Int) {
        view.findViewById<Button>(idDetalle).setOnClickListener {
            // Lógica de navegación a DetalleProyectoFragment
            val fragmentoDetalle = DetalleProyectoFragment().apply {
                arguments = Bundle().apply {
                    putString("nombre_proyecto", nombreProyecto)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoDetalle) // Asegúrate que este ID existe en tu Activity principal
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(idEditar).setOnClickListener {
            Toast.makeText(context, "Abriendo edición de $nombreProyecto", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(idEliminar).setOnClickListener {
            Toast.makeText(context, "$nombreProyecto eliminado", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "Acción rápida de transporte", Toast.LENGTH_SHORT).show()
        }
    }
}