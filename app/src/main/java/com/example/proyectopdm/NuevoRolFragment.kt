package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class NuevoRolFragment : Fragment(R.layout.fragment_nuevo_rol) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular los botones del formulario
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarNuevoRol)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarNuevoRol)

        // Función local para regresar al listado de roles
        fun regresarAListaRoles() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RolesListaFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // Eventos de clic
        btnCancelar.setOnClickListener {
            regresarAListaRoles()
        }

        btnConfirmar.setOnClickListener {
            // Aquí irá la lógica para guardar el nuevo rol en la base de datos
            regresarAListaRoles()
        }

        // Interceptar el gesto de retroceso del teléfono
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAListaRoles()
                }
            }
        )
    }
}