package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonalFragment : Fragment(R.layout.fragment_personal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Localizamos el botón flotante
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregar)

        // Al hacer clic, usamos la función de la MainActivity para cambiar de pantalla
        // PersonalFragment.kt - CAMBIA ESTO:
        fabAgregar.setOnClickListener {
            // En lugar de usar la función de MainActivity, usa el FragmentManager directamente:
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarPersonalFragment())
                .addToBackStack(null) // <--- ESTO ES LO QUE HACE QUE EL BOTÓN CANCELAR SIRVA
                .commit()
        }
    }
}