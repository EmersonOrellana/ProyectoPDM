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

        val btnBack = view.findViewById<ImageView>(R.id.btnBackVerPerfil)
        val btnEditarPerfil = view.findViewById<Button>(R.id.btnIrAEditarPerfil)

        // Función local para regresar al menú principal de Ajustes (PerfilFragment)
        fun regresarAlMenuAjustes() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, PerfilFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "AJUSTES"
        }

        // Navegar a la pantalla de edición de perfil
        btnEditarPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ActualizarPerfilFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // Clic en la flecha física/visual superior para regresar a ajustes
        btnBack.setOnClickListener {
            regresarAlMenuAjustes()
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