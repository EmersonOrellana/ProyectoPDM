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

        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarPerfil)

        btnActualizar.setOnClickListener {
            // Lógica de guardado aquí

            // Regresar a Ver Perfil
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, VerPerfilFragment())
                .commit()

            // Actualizar título del header
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "MI PERFIL"
        }
    }
}