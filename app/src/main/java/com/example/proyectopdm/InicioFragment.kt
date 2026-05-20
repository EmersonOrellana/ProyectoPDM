package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botones del Proyecto X (Como ejemplo)
        val btnEliminarX = view.findViewById<Button>(R.id.btnEliminarX)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarProyecto)

        fabAgregar.setOnClickListener {
            Toast.makeText(requireContext(), "Nuevo Proyecto", Toast.LENGTH_SHORT).show()
        }
    }
}