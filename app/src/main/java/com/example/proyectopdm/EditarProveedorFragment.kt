package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class EditarProveedorFragment : Fragment(R.layout.fragment_editar_proveedor) {

    private var idProveedorActual: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Vincular los EditText y Botones
        val etNombre = view.findViewById<EditText>(R.id.etEditNombreProveedor)
        val etDireccion = view.findViewById<EditText>(R.id.etEditDireccionProveedor)
        val etContacto = view.findViewById<EditText>(R.id.etEditContactoProveedor)
        val etTelefono = view.findViewById<EditText>(R.id.etEditTelefonoProveedor)
        val etCorreo = view.findViewById<EditText>(R.id.etEditCorreoProveedor)
        val etNotas = view.findViewById<EditText>(R.id.etEditNotasProveedor)

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarProveedor)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditProveedor)

        // 2. Recuperar el ID que le mandamos desde la lista
        idProveedorActual = arguments?.getInt("ID_PROVEEDOR") ?: -1
        val repo = ProveedorRepository(requireContext())

        // 3. Cargar los datos a los campos de texto
        if (idProveedorActual != -1) {
            val proveedor = repo.obtenerProveedorPorId(idProveedorActual)
            proveedor?.let {
                etNombre.setText(it.nombreProveedor)
                etDireccion.setText(it.direccionProveedor)
                etTelefono.setText(it.telefonoProveedor)
                etCorreo.setText(it.correoProveedor)

                // Lógica para separar el Contacto de las Notas (si las juntamos antes)
                val notaCompleta = it.notaProveedor
                if (notaCompleta.startsWith("Contacto: ")) {
                    val partes = notaCompleta.split("\nNotas: ")
                    etContacto.setText(partes[0].replace("Contacto: ", ""))
                    if (partes.size > 1) {
                        etNotas.setText(partes[1])
                    }
                } else {
                    etNotas.setText(notaCompleta)
                }
            }
        }

        // 4. Acción de Cancelar
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 5. Acción de Guardar Cambios
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val contacto = etContacto.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val notas = etNotas.text.toString().trim()

            // Validar campos obligatorios
            if (nombre.isNotEmpty() && telefono.isNotEmpty()) {

                // Volver a juntar el contacto y la nota
                val notaFinal = if (contacto.isNotEmpty()) "Contacto: $contacto\nNotas: $notas" else notas

                val proveedorActualizado = Proveedor(
                    idProveedor = idProveedorActual,
                    nombreProveedor = nombre,
                    direccionProveedor = direccion,
                    telefonoProveedor = telefono,
                    correoProveedor = correo,
                    notaProveedor = notaFinal
                )

                // Enviar a guardar y regresar
                if (repo.actualizarProveedor(proveedorActualizado)) {
                    Toast.makeText(context, "Proveedor actualizado correctamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "El nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}