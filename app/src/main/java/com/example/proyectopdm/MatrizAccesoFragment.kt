package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class MatrizAccesoFragment : Fragment(R.layout.fragment_matriz_acceso) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ya no buscamos el btnBackMatriz, solo los botones del formulario
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarMatriz)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMatriz)

        // 2. Función interna para regresar a la pantalla de Ajustes
        fun regresarAlPerfil() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, PerfilFragment())
                .commit()

            // Restablece el título del encabezado superior
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "AJUSTES"
        }

        // 3. Eventos de los botones principales
        btnCancelar.setOnClickListener {
            regresarAlPerfil()
        }

        btnGuardar.setOnClickListener {
            // Lógica para guardar la configuración de permisos
            regresarAlPerfil()
        }

        // 4. CONTROL DE GESTOS Y BOTONES DIGITALES DEL SISTEMA
        // Esto es lo que captura cuando deslizas el dedo desde el borde o tocas la flecha digital de Android
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAlPerfil()
                }
            }
        )
    }
}