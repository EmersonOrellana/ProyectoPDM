package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RolesListaFragment : Fragment(R.layout.fragment_lista_roles) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular botones de la interfaz
        val btnBack = view.findViewById<ImageView>(R.id.btnBackListaRoles)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarRol)

        // Botones de las tarjetas quemadas (Admin y Operativo)
        val btnEditarAdmin = view.findViewById<MaterialButton>(R.id.btnEditarRolAdmin)
        val btnEliminarAdmin = view.findViewById<MaterialButton>(R.id.btnEliminarRolAdmin)
        val btnEditarOperativo = view.findViewById<MaterialButton>(R.id.btnEditarRolOperativo)
        val btnEliminarOperativo = view.findViewById<MaterialButton>(R.id.btnEliminarRolOperativo)

        // 2. Función para regresar al menú de Ajustes (Perfil)
        fun regresarAlMenuAjustes() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, PerfilFragment())
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "AJUSTES"
        }

        // 3. Eventos de navegación hacia los formularios
        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, NuevoRolFragment())
                .commit()
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        val irAEditarRol = View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, EditarRolFragment())
                .commit()
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        btnEditarAdmin.setOnClickListener(irAEditarRol)
        btnEditarOperativo.setOnClickListener(irAEditarRol)

        // 4. Eventos de eliminación
        btnEliminarAdmin.setOnClickListener {
            Toast.makeText(context, "Eliminar Rol: Administrador", Toast.LENGTH_SHORT).show()
        }

        btnEliminarOperativo.setOnClickListener {
            Toast.makeText(context, "Eliminar Rol: Operativo", Toast.LENGTH_SHORT).show()
        }

        // 5. Control de retroceso (Flecha visual y gestos del celular)
        btnBack?.setOnClickListener {
            regresarAlMenuAjustes()
        }

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