package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonalFragment : Fragment(R.layout.fragment_personal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Localizamos los componentes de la tarjeta
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregar)
        val btnFicha = view.findViewById<MaterialButton>(R.id.btnFicha)
        val btnProyecto = view.findViewById<MaterialButton>(R.id.btnProyecto)
        val btnEditar = view.findViewById<MaterialButton>(R.id.btnEditar)
        val btnBaja = view.findViewById<MaterialButton>(R.id.btnBaja)

        btnEditar?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, EditarPersonalFragment()) // Llama a tu fragmento de edición
                .addToBackStack(null) // Permite volver atrás
                .commit()

            // Cambiamos el título en el Header
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Editar Personal"
        }

        // 2. Navegación a Ficha del Empleado
        btnFicha?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, FichaEmpleadoFragment()) // Asegúrate que este sea el ID correcto
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Ficha de Empleado"
        }

        // 3. Navegación a Proyectos Asignados
        btnProyecto?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ProyectosAsignadosFragment()) // Cambia por el nombre de tu fragmento de proyectos
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Proyectos Asignados"
        }

        // 4. Botón Flotante para agregar nuevo personal
        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarPersonalFragment())
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Registrar Personal"
        }
    }
}