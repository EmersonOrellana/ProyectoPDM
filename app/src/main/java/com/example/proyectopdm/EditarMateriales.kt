package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class EditarMaterialFragment : Fragment(R.layout.fragment_editar_materiales) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Enlazar componentes del XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreMaterial)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoriaMaterial)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidadMaterial)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionMaterial)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditar)

        // 2. Poblar el Spinner de Categorías de muestra
        val categorias = listOf("Obra Gris", "Acabados", "Fontanería", "Electricidad", "Herramientas")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        // Prellenar con los datos actuales de prueba (Mockup)
        etNombre.setText("Cemento holcim fuerte")
        etUnidad.setText("Sacos")

        // 3. Comportamiento del botón Actualizar
        btnActualizar.setOnClickListener {
            val nombreTxt = etNombre.text.toString()
            if (nombreTxt.isNotEmpty()) {
                Toast.makeText(requireContext(), "¡$nombreTxt actualizado exitosamente!", Toast.LENGTH_SHORT).show()

                // Regresar a la lista de Materiales
                (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
            } else {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Comportamiento del botón Cancelar
        btnCancelar.setOnClickListener {
            // Volver directo sin guardar nada
            (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
        }
    }
}