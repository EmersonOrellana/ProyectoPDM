package com.example.proyectopdm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MaterialesFragment : Fragment(R.layout.fragment_materiales) {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvMateriales: RecyclerView
    private lateinit var etBuscar: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        rvMateriales = view.findViewById(R.id.rvListaMateriales)
        etBuscar = view.findViewById(R.id.etBuscarMaterial)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarMaterial)

        // Carga inicial
        actualizarLista(dbHelper.obtenerMateriales())

        // Configurar buscador
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                actualizarLista(if (query.isEmpty()) dbHelper.obtenerMateriales() else dbHelper.buscarMateriales(query))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fabAgregar?.setOnClickListener {
            (activity as? MainActivity)?.cambiarPantalla(RegistrarMaterialFragment(), R.id.nav_materiales, "REGISTRAR")
        }
    }

    private fun actualizarLista(lista: List<Material>) {
        // Ahora pasamos DOS lambdas: una para bajar/eliminar y otra para editar
        rvMateriales.adapter = MaterialAdapter(
            lista,
            onBajaClick = { material ->
                ConfirmarDialog.newInstance(
                    titulo = "¿Dar de baja ${material.nombre}?",
                    textoBoton = "Sí, eliminar",
                    accion = {
                        if (dbHelper.eliminarMaterial(material.idMaterial)) {
                            Toast.makeText(requireContext(), "Material eliminado", Toast.LENGTH_SHORT).show()
                            actualizarLista(dbHelper.obtenerMateriales())
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }
                ).show(parentFragmentManager, "dialog_baja")
            },
            onEditarClick = { material ->
                // Abrir pantalla de edición pasando el objeto material
                (activity as? MainActivity)?.cambiarPantalla(
                    EditarMaterialFragment(material),
                    R.id.nav_materiales,
                    "EDITAR_MATERIAL"
                )
            }
        )
    }
}