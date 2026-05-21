package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class DetalleProyectoFragment : Fragment() {

    private lateinit var txtNombreProyecto: TextView
    private lateinit var spEstado: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle_proyecto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas del encabezado
        txtNombreProyecto = view.findViewById(R.id.txtDetalleNombreProyecto)
        spEstado = view.findViewById(R.id.spDetalleEstado)
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtrasDetalle)

        // Inicializar botones de acciones
        val btnElegirEncargado = view.findViewById<Button>(R.id.btnElegirEncargado)
        val btnAgregarMateriales = view.findViewById<Button>(R.id.btnAgregarMateriales)
        val btnAgregarCotizacion = view.findViewById<Button>(R.id.btnAgregarCotizacion)
        val btnComparar = view.findViewById<Button>(R.id.btnCompararCotizaciones)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarDetalles)

        // Recuperar el nombre del proyecto enviado desde la lista
        val nombreProyecto = arguments?.getString("nombre_proyecto") ?: "Proyecto"
        txtNombreProyecto.text = nombreProyecto

        // Opciones actualizadas para el Spinner de estados
        val estadosArray = arrayOf("Iniciado", "En proceso", "Finalizado")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estadosArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = adapter

        // Preseleccionar el estado simulado según el proyecto que viene de la lista
        when (nombreProyecto) {
            "Proyecto X" -> spEstado.setSelection(0) // Iniciado
            "Proyecto Y" -> spEstado.setSelection(1) // En proceso
            "Proyecto Z" -> spEstado.setSelection(2) // Finalizado
        }

        // Listeners para regresar sin romper el flujo de la MainActivity
        btnAtras.setOnClickListener { parentFragmentManager.popBackStack() }

        // Evento para abrir el Diálogo de Seleccionar Encargado
        btnElegirEncargado.setOnClickListener {
            SeleccionarEncargadoDialog().show(parentFragmentManager, "SeleccionarEncargadoDialog")
        }

        btnAgregarMateriales.setOnClickListener {
            AgregarMaterialDialog().show(parentFragmentManager, "AgregarMaterialDialog")
        }

        // CONEXIÓN: Abre la pantalla de Registro de Cotización (Vista Previa)
        btnAgregarCotizacion.setOnClickListener {
            val fragmentCotizacion = AgregarCotizacionFragment()
            val bundle = Bundle()
            bundle.putString("nombre_proyecto", nombreProyecto)
            fragmentCotizacion.arguments = bundle

            val idContenedor = (view.parent as ViewGroup).id

            parentFragmentManager.beginTransaction()
                .replace(idContenedor, fragmentCotizacion)
                .addToBackStack(null) // Para que al dar "Atrás" regrese al detalle perfectamente
                .commit()
        }

        // CONEXIÓN COMPLETA: Abre la pantalla general de Comparación de Cotizaciones
        btnComparar.setOnClickListener {
            val fragmentoComparar = CompararCotizacionesFragment()
            val bundle = Bundle()
            bundle.putString("nombre_proyecto", nombreProyecto)
            fragmentoComparar.arguments = bundle

            // Buscamos el ID del contenedor dinámicamente para inyectar la vista completa
            val idContenedor = (view.parent as ViewGroup).id

            parentFragmentManager.beginTransaction()
                .replace(idContenedor, fragmentoComparar)
                .addToBackStack(null) // Guarda el estado en la pila para volver sin problemas
                .commit()
        }

        btnGuardar.setOnClickListener {
            val estadoSeleccionado = spEstado.selectedItem.toString()
            Toast.makeText(context, "Cambios de $nombreProyecto guardados como [$estadoSeleccionado]", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}