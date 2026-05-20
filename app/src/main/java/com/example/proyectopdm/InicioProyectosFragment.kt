package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InicioProyectosFragment : Fragment() {

    private lateinit var edtBuscarProyecto: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño XML de la lista de proyectos
        return inflater.inflate(R.layout.fragment_inicio_proyectos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar componentes generales
        edtBuscarProyecto = view.findViewById(R.id.edtBuscarProyecto)

        // 2. Configurar eventos independientes para cada tarjeta de proyecto
        configurarProyectoX(view)
        configurarProyectoY(view)
        configurarProyectoZ(view)

        // 3. Configurar acciones de los botones flotantes de la esquina inferior derecha
        configurarBotonesFlotantes(view)
    }

    private fun configurarBotonesFlotantes(view: View) {
        val fabAgregarProyecto = view.findViewById<FloatingActionButton>(R.id.fabAgregarProyecto)
        val fabAgregarTransporte = view.findViewById<FloatingActionButton>(R.id.fabAgregarTransporte)

        fabAgregarProyecto.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, CrearProyectoFragment())
                .addToBackStack(null)
                .commit()
        }

        fabAgregarTransporte.setOnClickListener {
            Toast.makeText(context, "Abrir formulario: Nuevo Transporte", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarProyectoX(view: View) {
        val btnVerX = view.findViewById<Button>(R.id.btnVerDetallesX)
        val btnEditarX = view.findViewById<Button>(R.id.btnEditarX)
        val btnEliminarX = view.findViewById<Button>(R.id.btnEliminarX)

        // Lógica para abrir los detalles de Proyecto X pasando el argumento de texto
        btnVerX.setOnClickListener {
            val fragmentoDetalle = DetalleProyectoFragment().apply {
                arguments = Bundle().apply {
                    putString("nombre_proyecto", "Proyecto X")
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoDetalle)
                .addToBackStack(null)
                .commit()
        }

        btnEditarX.setOnClickListener {
            Toast.makeText(context, "Abriendo edición de Proyecto X", Toast.LENGTH_SHORT).show()
        }

        btnEliminarX.setOnClickListener {
            Toast.makeText(context, "Proyecto X eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarProyectoY(view: View) {
        val btnVerY = view.findViewById<Button>(R.id.btnVerDetallesY)
        val btnEditarY = view.findViewById<Button>(R.id.btnEditarY)
        val btnEliminarY = view.findViewById<Button>(R.id.btnEliminarY)

        // Lógica para abrir los detalles de Proyecto Y pasando el argumento de texto
        btnVerY.setOnClickListener {
            val fragmentoDetalle = DetalleProyectoFragment().apply {
                arguments = Bundle().apply {
                    putString("nombre_proyecto", "Proyecto Y")
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoDetalle)
                .addToBackStack(null)
                .commit()
        }

        btnEditarY.setOnClickListener {
            Toast.makeText(context, "Abriendo edición de Proyecto Y", Toast.LENGTH_SHORT).show()
        }

        btnEliminarY.setOnClickListener {
            Toast.makeText(context, "Proyecto Y eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarProyectoZ(view: View) {
        val btnVerZ = view.findViewById<Button>(R.id.btnVerDetallesZ)
        val btnEditarZ = view.findViewById<Button>(R.id.btnEditarZ)
        val btnEliminarZ = view.findViewById<Button>(R.id.btnEliminarZ)

        // Lógica para abrir los detalles de Proyecto Z pasando el argumento de texto
        btnVerZ.setOnClickListener {
            val fragmentoDetalle = DetalleProyectoFragment().apply {
                arguments = Bundle().apply {
                    putString("nombre_proyecto", "Proyecto Z")
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoDetalle)
                .addToBackStack(null)
                .commit()
        }

        btnEditarZ.setOnClickListener {
            Toast.makeText(context, "Abriendo edición de Proyecto Z", Toast.LENGTH_SHORT).show()
        }

        btnEliminarZ.setOnClickListener {
            Toast.makeText(context, "Proyecto Z eliminado", Toast.LENGTH_SHORT).show()
        }
    }
}