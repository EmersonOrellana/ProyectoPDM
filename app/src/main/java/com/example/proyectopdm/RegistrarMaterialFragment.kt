package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class RegistrarMaterialFragment : Fragment(R.layout.fragment_registrar_material) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Enlazar componentes del XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreNuevoMaterial)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoriaNuevoMaterial)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidadNuevoMaterial)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionNuevoMaterial)
        val btnCrear = view.findViewById<Button>(R.id.btnCrearMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarRegistrar)

        // 2. Poblar el Spinner de Categorías (Igual para mantener consistencia)
        val categorias = listOf("Obra Gris", "Acabados", "Fontanería", "Electricidad", "Herramientas")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        // 3. Lógica del botón Crear Material
        btnCrear.setOnClickListener {
            val nombreTxt = etNombre.text.toString().trim()
            val unidadTxt = etUnidad.text.toString().trim()

            if (nombreTxt.isNotEmpty() && unidadTxt.isNotEmpty()) {
                Toast.makeText(requireContext(), "¡$nombreTxt registrado exitosamente!", Toast.LENGTH_SHORT).show()

                // Regresa automáticamente a la lista principal de Materiales
                (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
            } else {
                Toast.makeText(requireContext(), "Por favor, llena los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Lógica del botón Cancelar
        btnCancelar.setOnClickListener {
            // Regresa sin guardar cambios
            (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
        }
    }
}