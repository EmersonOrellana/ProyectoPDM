package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class ProyectosAsignadosFragment : Fragment(R.layout.fragment_proyectos_asignados) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular los botones de las tarjetas
        val btnProyectoX = view.findViewById<Button>(R.id.btnDetalleProyectoX)
        val btnProyectoF = view.findViewById<Button>(R.id.btnDetalleProyectoF)
        val btnProyectoA = view.findViewById<Button>(R.id.btnDetalleProyectoA)

        // 2. Función para navegar al detalle
        fun irADetalle(nombreProyecto: String) {
            val fragmentDetalle = DetalleProyectoFragment()

            // Pasamos el nombre del proyecto como argumento al fragmento destino
            val bundle = Bundle()
            bundle.putString("nombre_proyecto", nombreProyecto)
            fragmentDetalle.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentDetalle)
                .addToBackStack(null) // Permite regresar a la lista de proyectos al dar atrás
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Detalle del Proyecto"
        }

        // 3. Asignar clics a los botones
        btnProyectoX.setOnClickListener { irADetalle("Proyecto X") }
        btnProyectoF.setOnClickListener { irADetalle("Proyecto F") }
        btnProyectoA.setOnClickListener { irADetalle("Proyecto A") }

        // 4. Manejo del botón atrás para regresar a la Ficha
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, FichaEmpleadoFragment())
                        .commit()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
                }
            }
        )
    }
}