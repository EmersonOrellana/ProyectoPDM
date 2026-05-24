package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class EditarMaterialFragment(private val material: Material) : Fragment(R.layout.fragment_editar_materiales) {

    private lateinit var dbHelper: DatabaseHelper
    private var listaCategorias = listOf<Categoria>()
    private var listaUnidades = listOf<Unidad>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())

        val etNombre = view.findViewById<EditText>(R.id.etNombreMaterial)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoriaMaterial)
        val spUnidad = view.findViewById<Spinner>(R.id.spUnidadMaterial)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionMaterial)
        val btnActualizar = view.findViewById<Button>(R.id.btnActualizarMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditar)

        // 1. Cargar datos reales
        listaCategorias = dbHelper.obtenerCategorias()
        listaUnidades = dbHelper.recuperarUnidadesMedida()

        // 2. Configurar adaptadores
        val adapterCat = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCategorias.map { it.nombreCategoria })
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapterCat

        val adapterUni = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaUnidades.map { it.nombreUnidad })
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidad.adapter = adapterUni

        // 3. Preseleccionar valores actuales (ESTO CORRIGE EL ID 1 AUTOMÁTICO)
        // Buscamos la posición de la categoría y unidad que ya tiene el material
        val posCat = listaCategorias.indexOfFirst { it.nombreCategoria == material.nombreCategoria }
        val posUni = listaUnidades.indexOfFirst { it.nombreUnidad == material.nombreUnidad }

        if (posCat != -1) spCategoria.setSelection(posCat)
        if (posUni != -1) spUnidad.setSelection(posUni)

        etNombre.setText(material.nombre)
        etDescripcion.setText(material.descripcion)

        // 4. Botón Actualizar (Lógica real)
        btnActualizar.setOnClickListener {
            // AQUÍ ESTABA EL ERROR: Usamos la posición del spinner para sacar el ID real de la lista
            val idCat = listaCategorias[spCategoria.selectedItemPosition].idCategoria
            val idUni = listaUnidades[spUnidad.selectedItemPosition].idUnidad

            val exito = dbHelper.actualizarMaterial(
                material.idMaterial,
                etNombre.text.toString(),
                idCat,
                idUni,
                etDescripcion.text.toString()
            )

            if (exito) {
                Toast.makeText(requireContext(), "Actualizado!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
            }
        }

        // 5. Botón Cancelar (Corregido)
        btnCancelar.setOnClickListener {
            (activity as? MainActivity)?.cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
        }
    }
}