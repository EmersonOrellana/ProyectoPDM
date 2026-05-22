package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class EditarTransportistaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editar_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar Spinner
        val spinnerLicencia = view.findViewById<Spinner>(R.id.spTipoLicenciaEditar)
        val opciones = arrayOf("Pesada", "Pesada T", "Liviana", "Particular")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLicencia.adapter = adapter

        // 2. Botón Atrás y Cancelar
        view.findViewById<ImageButton>(R.id.btnAtrasEditar).setOnClickListener { parentFragmentManager.popBackStack() }
        view.findViewById<Button>(R.id.btnCancelarEditar).setOnClickListener { parentFragmentManager.popBackStack() }

        // 3. Botón Actualizar
        view.findViewById<Button>(R.id.btnActualizarUsuario).setOnClickListener {
            Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}