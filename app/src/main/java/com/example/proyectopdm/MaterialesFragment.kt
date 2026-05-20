package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MaterialesFragment : Fragment(R.layout.fragment_materiales) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular los componentes del XML corregido
        val etBuscar = view.findViewById<EditText>(R.id.etBuscarMaterial)
        val cardMaterial = view.findViewById<CardView>(R.id.cardMaterial)
        val btnEditar = view.findViewById<Button>(R.id.btnEditarMaterial)
        val btnBaja = view.findViewById<Button>(R.id.btnBajaMaterial)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarMaterial)


        // 2. Configurar las acciones de los clicks
        // Dentro del onViewCreated de tu MaterialesFragment.kt original, cambia el listener del botón editar:
        btnEditar.setOnClickListener {
            // Llama a la función del MainActivity para hacer la transición limpia de fragmento
            (activity as? MainActivity)?.cambiarPantalla(
                EditarMaterialFragment(),
                R.id.nav_materiales,
                "MATERIALES"
            )
        }

        btnBaja.setOnClickListener {
            // Creamos y mostramos la ventana flotante en una sola línea
            ConfirmarDialog.newInstance(
                titulo = "¿Dar de baja Cemento Holcim?",
                textoBoton = "Sí, dar de baja",
                accion = {
                    // Todo lo que escribas aquí se ejecutará SOLO si presionas el botón rojo
                    Toast.makeText(requireContext(), "¡Material inactivado con éxito!", Toast.LENGTH_SHORT).show()
                }
            ).show(parentFragmentManager, "dialog_baja")
        }

        // Dentro del onViewCreated de tu MaterialesFragment.kt original, edita el evento del botón agregar:
        fabAgregar?.setOnClickListener {
            // Abre la nueva pantalla de registro de materiales usando el cargador global
            (activity as? MainActivity)?.cambiarPantalla(
                RegistrarMaterialFragment(),
                R.id.nav_materiales,
                "MATERIALES"
            )
        }
    }
}