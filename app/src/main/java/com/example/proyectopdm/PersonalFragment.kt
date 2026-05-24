package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonalFragment : Fragment(R.layout.fragment_personal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Localizamos los componentes
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregar)

        // AQUÍ CONECTAMOS LA FICHA: Cambia "btnVerFichaEmpleado" por el ID real de tu tarjeta de empleado
        val btnVerFicha = view.findViewById<View>(R.id.btnFicha)

        // Botón Flotante para agregar nuevo personal
        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarPersonalFragment())
                .addToBackStack(null) // <--- ESTO ES LO QUE HACE QUE EL BOTÓN CANCELAR SIRVA
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // Evento para abrir la Ficha del Empleado
        btnVerFicha?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, FichaEmpleadoFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }
    }
}