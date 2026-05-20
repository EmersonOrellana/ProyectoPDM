package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment

class RegistrarPersonalFragment : Fragment(R.layout.fragment_registrar_personal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del desplegable de Rol
        val roles = listOf("Coordinador", "Operativo", "Administrativo", "Técnico")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        val autoComplete = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteRol)
        autoComplete.setAdapter(adapter)

        // Botón Cancelar: Vuelve a la pantalla anterior view.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
        view.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Botón Crear Usuario: Aquí iría tu lógica de guardado
        view.findViewById<Button>(R.id.btnCrearUsuario).setOnClickListener {
            // Lógica pendiente de base de datos
        }
    }
}