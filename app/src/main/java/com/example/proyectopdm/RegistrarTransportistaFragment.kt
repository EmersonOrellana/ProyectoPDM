package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class RegistrarTransportistaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el XML que creamos en el paso anterior
        return inflater.inflate(R.layout.fragment_registrar_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configurar el Spinner de Tipo de Licencia
        val spinnerLicencia = view.findViewById<Spinner>(R.id.spTipoLicencia)
        val opcionesLicencia = arrayOf("Pesada", "Pesada T", "Liviana", "Particular")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesLicencia)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLicencia.adapter = adapter

        // 2. Configurar botón Atrás para regresar a la lista de transportistas
        view.findViewById<ImageButton>(R.id.btnAtrasRegistrar).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Configurar botón Cancelar
        view.findViewById<Button>(R.id.btnCancelarRegistro).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 4. Configurar botón Crear Usuario (Guardar)
        view.findViewById<Button>(R.id.btnCrearUsuario).setOnClickListener {
            Toast.makeText(context, "Transportista registrado con éxito", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}