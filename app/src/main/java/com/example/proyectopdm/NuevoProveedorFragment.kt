package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class NuevoProveedorFragment : Fragment(R.layout.fragment_nuevo_proveedor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular todos los EditText del XML
        val etNombre = view.findViewById<EditText>(R.id.etNombreProveedor)
        val etDireccion = view.findViewById<EditText>(R.id.etDireccionProveedor)
        val etContacto = view.findViewById<EditText>(R.id.etContactoProveedor)
        val etTelefono = view.findViewById<EditText>(R.id.etTelefonoProveedor)
        val etCorreo = view.findViewById<EditText>(R.id.etCorreoProveedor)
        val etNotas = view.findViewById<EditText>(R.id.etNotasProveedor)

        // 2. Vincular los Botones
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarProveedor)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarProveedor)

        // Evento: Botón Cancelar
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Evento: Botón Agregar
        btnAgregar.setOnClickListener {
            // Capturar la información escrita por el usuario
            val nombre = etNombre.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val contacto = etContacto.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val notas = etNotas.text.toString().trim()

            // Validación básica: Exigir nombre y teléfono
            if (nombre.isNotEmpty() && telefono.isNotEmpty()) {

                // Unimos el contacto y las notas para que quepa en la columna NOTA_PROVEEDOR
                val notaFinal = if (contacto.isNotEmpty()) "Contacto: $contacto\nNotas: $notas" else notas

                // Creamos el objeto con los datos
                val nuevoProveedor = Proveedor(
                    nombreProveedor = nombre,
                    direccionProveedor = direccion,
                    telefonoProveedor = telefono,
                    correoProveedor = correo,
                    notaProveedor = notaFinal
                )

                // Llamamos a nuestro nuevo repositorio
                val repo = ProveedorRepository(requireContext())
                val idGuardado = repo.insertarProveedor(nuevoProveedor)

                if (idGuardado != -1L) {
                    Toast.makeText(context, "Proveedor guardado exitosamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack() // Cierra el fragmento y vuelve a la lista
                } else {
                    Toast.makeText(context, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Por favor, ingresa al menos el Nombre y Teléfono", Toast.LENGTH_SHORT).show()
            }
        }
    }
}