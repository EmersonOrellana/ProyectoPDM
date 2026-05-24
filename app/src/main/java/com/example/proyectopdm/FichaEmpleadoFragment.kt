package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FichaEmpleadoFragment : Fragment(R.layout.fragment_ficha_empleado) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular el botón "Ver Proyectos" del XML
        val btnVerProyectos = view.findViewById<Button>(R.id.btnVerProyectos)

        // 2. Evento: Navegar a los proyectos del empleado
        btnVerProyectos.setOnClickListener {
            // Realizamos la transacción para reemplazar el fragmento actual por el de proyectos
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ProyectosAsignadosFragment()) // Asegúrate que este nombre de clase exista
                .addToBackStack(null) // Esto permite al usuario volver atrás con el botón físico
                .commit()

            // Opcional: Actualizar el título de la barra superior si la tienes en tu Activity
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Proyectos Asignados"
        }

        // 3. Función para regresar al listado general de Personal al presionar atrás
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, PersonalFragment())
                        .commit()

                    // Restaurar el título de la barra superior
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "PERSONAL"
                }
            }
        )
    }
}