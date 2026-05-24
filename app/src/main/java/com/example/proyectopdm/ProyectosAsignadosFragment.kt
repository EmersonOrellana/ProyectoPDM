package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class ProyectosAsignadosFragment : Fragment(R.layout.fragment_proyectos_asignados) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Función para regresar exclusivamente a la ficha del empleado
        fun regresarAFicha() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, FichaEmpleadoFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAFicha()
                }
            }
        )
    }
}