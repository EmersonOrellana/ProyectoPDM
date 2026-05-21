package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProveedoresFragment : Fragment(R.layout.fragment_proveedores) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular los componentes del XML con Kotlin
        val etBuscar = view.findViewById<EditText>(R.id.etBuscarProveedor)
        val cardProveedor = view.findViewById<CardView>(R.id.cardProveedor)
        val btnEditar = view.findViewById<Button>(R.id.btnEditarProveedor)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminarProveedor)
        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarProveedor)

        // 2. Configurar el evento para el Botón Editar
        btnEditar.setOnClickListener {
            // Aquí irá la lógica o navegación para modificar el proveedor

            //Navegacion al fragmento de Editar:
            (activity as MainActivity).cambiarPantalla(
                EditarProveedorFragment(),
                R.id.nav_proveedores,
                "COTMAN" // O el título que prefieras que aparezca en el header azul
            )

            Toast.makeText(requireContext(), "Editar Proveedor 1", Toast.LENGTH_SHORT).show()
        }

        // 3. Configurar el evento para el Botón Eliminar
        btnEliminar.setOnClickListener {
            // Aquí irá la lógica para borrar el registro (por ejemplo, de la base de datos)
            Toast.makeText(requireContext(), "Eliminar Proveedor 1", Toast.LENGTH_SHORT).show()
        }

        // 4. Configurar el evento para el Botón Flotante (+)
        fabAgregar.setOnClickListener {
            // Aquí abrirás el formulario o diálogo para registrar un nuevo proveedor

            //Navegacion al fragmento de Nuevo proveedor:
            (activity as MainActivity).cambiarPantalla(
                NuevoProveedorFragment(),
                R.id.nav_proveedores,
                "COTMAN"
            )

            Toast.makeText(requireContext(), "Agregar nuevo proveedor", Toast.LENGTH_SHORT).show()
        }
    }
}