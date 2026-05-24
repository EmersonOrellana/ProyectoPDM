package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class RegistrarMaterialFragment : Fragment(R.layout.fragment_registrar_material) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = DatabaseHelper(requireContext())

        val etNombre = view.findViewById<EditText>(R.id.etNombreNuevoMaterial)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoriaNuevoMaterial)
        val spUnidad = view.findViewById<Spinner>(R.id.spUnidadMedidaMaterial)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionNuevoMaterial)
        val btnCrear = view.findViewById<Button>(R.id.btnCrearMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarRegistrar)

        // 1. Categorías
        val listaCategorias = dbHelper.obtenerCategorias()
        val adapterCat = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCategorias.map { it.nombreCategoria })
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapterCat

        // 2. Unidades de Medida
        val listaUnidades = dbHelper.recuperarUnidadesMedida()
        val adapterUni = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaUnidades.map { it.nombreUnidad })
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidad.adapter = adapterUni

        btnCrear.setOnClickListener {
            val nombreTxt = etNombre.text.toString().trim()
            val descTxt = etDescripcion.text.toString().trim()

            if (listaCategorias.isEmpty() || listaUnidades.isEmpty()) {
                Toast.makeText(requireContext(), "Base de datos vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idCat = listaCategorias[spCategoria.selectedItemPosition].idCategoria
            val idUni = listaUnidades[spUnidad.selectedItemPosition].idUnidad

            if (nombreTxt.isNotEmpty()) {
                val exito = dbHelper.registrarMaterial(nombreTxt, idCat, idUni, descTxt)
                if (exito) {
                    Toast.makeText(requireContext(), "Registrado con éxito", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
                } else {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancelar.setOnClickListener {
            (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
        }
    }
}