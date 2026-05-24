package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FichaEmpleadoFragment : Fragment(R.layout.fragment_ficha_empleado) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular las vistas del XML

        val btnVerProyectos = view.findViewById<Button>(R.id.btnVerProyectos)

        // 2. Función para regresar al listado general de Personal
        fun regresarAPersonal() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, PersonalFragment())
                .commit()

            // Restaurar el título de la barra azul superior
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "PERSONAL"
        }

        // 3. Evento: Navegar a los proyectos del empleado
        btnVerProyectos.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ProyectosAsignadosFragment())
                .commit()

            // Mantenemos "COTMAN" como título al profundizar en la navegación
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAPersonal()
                }
            }
        )
    }
}