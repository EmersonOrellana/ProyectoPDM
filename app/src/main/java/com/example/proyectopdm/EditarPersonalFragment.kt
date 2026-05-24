package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class EditarPersonalFragment : Fragment(R.layout.fragment_editar_personal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del desplegable
        val roles = listOf("Coordinador", "Operativo", "Administrativo", "Técnico")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        val autoComplete = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditarRol)
        autoComplete.setAdapter(adapter)

        // Botón Guardar Cambios
        view.findViewById<Button>(R.id.btnGuardarCambios).setOnClickListener {
            Toast.makeText(context, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        // Botón Cancelar
        view.findViewById<Button>(R.id.btnCancelarEdicion).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}