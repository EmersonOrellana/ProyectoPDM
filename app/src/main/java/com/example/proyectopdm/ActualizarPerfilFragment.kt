package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class ActualizarPerfilFragment : Fragment(R.layout.fragment_actualizar_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackPerfil)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarPerfil)

        // Función local para volver a la pantalla de vista previa de perfil
        fun regresarAVerPerfil() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, VerPerfilFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        btnBack.setOnClickListener {
            regresarAVerPerfil()
        }

        btnActualizar.setOnClickListener {
            // Aquí irá tu código futuro de actualización (SQL/Firebase)
            regresarAVerPerfil()
        }

        // Capturar gestos del teléfono para regresar a la vista de perfil en vez de cerrar la app
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAVerPerfil()
                }
            }
        )
    }
}