package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MatrizAccesoFragment : Fragment(R.layout.fragment_matriz_acceso) {

    private lateinit var rvAccesos: RecyclerView
    private lateinit var spinnerRoles: Spinner
    private lateinit var adapter: MatrizAccesoAdapter
    private lateinit var repo: MatrizAccesoRepository

    private var listaRoles: List<RolModel> = listOf()
    private var rolSeleccionadoId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAccesos = view.findViewById(R.id.rv_matriz_accesos)
        spinnerRoles = view.findViewById(R.id.spinnerRolesMatriz)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarMatriz)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMatriz)

        rvAccesos.layoutManager = LinearLayoutManager(requireContext())
        repo = MatrizAccesoRepository(requireContext())

        // 1. Cargar Roles en el Spinner
        cargarRolesEnSpinner()

        // 2. Función para regresar a la pantalla anterior (Ajustes)
        fun volverAtras() {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            }
        }

        // 3. Evento: Cuando el usuario cambia de Rol en el menú
        spinnerRoles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                rolSeleccionadoId = listaRoles[position].idRol
                cargarMatrizParaRol(rolSeleccionadoId)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 4. Botón Guardar
        btnGuardar.setOnClickListener {
            if (rolSeleccionadoId != -1) {
                val datosActualizados = adapter.obtenerDatosActualizados()
                val exito = repo.guardarMatriz(rolSeleccionadoId, datosActualizados)

                if (exito) {
                    Toast.makeText(context, "Permisos guardados exitosamente", Toast.LENGTH_SHORT).show()
                    // REGRESO AUTOMÁTICO AL GUARDAR
                    volverAtras()
                } else {
                    Toast.makeText(context, "Error al guardar permisos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 5. Lógica de Retroceso (Cancelar y Botón físico)
        btnCancelar.setOnClickListener { volverAtras() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { volverAtras() }
        })
    }

    private fun cargarRolesEnSpinner() {
        listaRoles = repo.obtenerRoles()
        if (listaRoles.isNotEmpty()) {
            val nombresRoles = listaRoles.map { it.nombreRol }
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresRoles)
            spinnerRoles.adapter = spinnerAdapter

            // Selecciona el primer rol por defecto
            rolSeleccionadoId = listaRoles[0].idRol
            cargarMatrizParaRol(rolSeleccionadoId)
        } else {
            Toast.makeText(context, "No hay roles creados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarMatrizParaRol(idRol: Int) {
        val accesos = repo.obtenerMatrizPorRol(idRol)
        adapter = MatrizAccesoAdapter(accesos)
        rvAccesos.adapter = adapter
    }
}