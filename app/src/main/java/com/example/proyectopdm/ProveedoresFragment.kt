package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProveedoresFragment : Fragment(R.layout.fragment_proveedores) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregarProveedor)
        val contenedorProveedores = view.findViewById<LinearLayout>(R.id.llContenedorProveedores)

        // =========================================================
        // LÓGICA DINÁMICA: LEER DE BD Y DIBUJAR TARJETAS
        // =========================================================
        val repo = ProveedorRepository(requireContext())
        val listaProveedores = repo.obtenerProveedores()

        // Limpiamos el contenedor
        contenedorProveedores?.removeAllViews()

        for (proveedor in listaProveedores) {
            // Inflamos la tarjeta molde
            val vistaTarjeta = LayoutInflater.from(context).inflate(R.layout.item_proveedor, contenedorProveedores, false)

            // Buscamos los elementos visuales de ESA tarjeta específica
            val tvNombre = vistaTarjeta.findViewById<TextView>(R.id.tvNombreProveedor)
            val tvDatos = vistaTarjeta.findViewById<TextView>(R.id.tvDatosProveedor)
            val btnEditar = vistaTarjeta.findViewById<Button>(R.id.btnEditarProveedor)
            val btnEliminar = vistaTarjeta.findViewById<Button>(R.id.btnEliminarProveedor)

            // Asignamos los datos reales
            tvNombre.text = proveedor.nombreProveedor
            tvDatos.text = "${proveedor.telefonoProveedor}\n${proveedor.correoProveedor}\n${proveedor.direccionProveedor}"

            // Configurar botón Editar (Pasando el ID)
            btnEditar.setOnClickListener {
                val fragment = EditarProveedorFragment()
                val bundle = Bundle()
                bundle.putInt("ID_PROVEEDOR", proveedor.idProveedor)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .addToBackStack(null)
                    .commit()

                activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "EDITAR PROVEEDOR"
            }


            btnEliminar.setOnClickListener {
                // 1. Crear un cuadro de diálogo de confirmación
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Proveedor")
                    .setMessage("¿Estás seguro de que deseas eliminar a ${proveedor.nombreProveedor}? Esta acción no se puede deshacer.")
                    .setPositiveButton("Sí, eliminar") { dialog, _ ->

                        // 2. Si el usuario confirma, llamamos al repositorio
                        if (repo.eliminarProveedor(proveedor.idProveedor)) {
                            Toast.makeText(requireContext(), "Proveedor eliminado", Toast.LENGTH_SHORT).show()

                            // 3. Truco visual: Desaparecemos la tarjeta de la pantalla instantáneamente
                            contenedorProveedores?.removeView(vistaTarjeta)
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        // Si cancela, solo cerramos el cuadro
                        dialog.dismiss()
                    }
                    .show()
            }

            // Agregamos la tarjeta a la pantalla
            contenedorProveedores?.addView(vistaTarjeta)
        }
        // =========================================================

        // Navegación para Agregar Proveedor
        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, NuevoProveedorFragment())
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "NUEVO PROVEEDOR"
        }
    }
}