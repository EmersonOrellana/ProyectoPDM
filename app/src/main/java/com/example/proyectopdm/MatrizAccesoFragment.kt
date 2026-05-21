package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class MatrizAccesoFragment : Fragment(R.layout.fragment_matriz_acceso) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular los botones
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarMatriz)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMatriz)

        // 2. Función simplificada para regresar (popBackStack es la clave)
        fun volverAtras() {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            }
        }

        // 3. Eventos de los botones
        btnCancelar.setOnClickListener {
            volverAtras()
        }

        btnGuardar.setOnClickListener {
            // Aquí puedes añadir tu lógica de guardado
            volverAtras()
        }

        // 4. Manejo del botón físico de atrás de Android
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                volverAtras()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}