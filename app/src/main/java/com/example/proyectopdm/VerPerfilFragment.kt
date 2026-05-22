package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class VerPerfilFragment : Fragment(R.layout.fragment_ver_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnEditarPerfil = view.findViewById<Button>(R.id.btnIrAEditarPerfil)

        // Función local para regresar al menú principal de Ajustes (PerfilFragment)
        fun regresarAlMenuAjustes() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, VerPerfilFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "AJUSTES"
        }

        // Navegar a la pantalla de edición de perfil
        btnEditarPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ActualizarPerfilFragment())
                .addToBackStack(null) // Permite volver atrás
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "EDITAR PERFIL"
        }

        // Capturar gestos de deslizamiento laterales o botón físico "Atrás" del sistema
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAlMenuAjustes()
                }
            }
        )
    }
}