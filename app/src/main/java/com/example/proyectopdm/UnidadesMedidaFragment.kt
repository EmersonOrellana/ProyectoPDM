package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UnidadesMedidaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unidades_medida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón flotante (+) para agregar nueva unidad
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fab_agregar_unidad)
        fabAgregar.setOnClickListener {
            Toast.makeText(context, "Abrir formulario de Nueva Unidad", Toast.LENGTH_SHORT).show()
        }
    }
}