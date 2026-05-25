package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class DetalleProyectoFragment : Fragment(), SeleccionarEncargadoDialog.OnEncargadoSeleccionado {

    private lateinit var txtNombreProyecto: TextView
    private lateinit var txtNombreEncargado: TextView
    private lateinit var spEstado: Spinner
    private var idProyectoActual: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle_proyecto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Obtención de datos
        idProyectoActual = arguments?.getInt("id_proyecto") ?: 0
        val nombreProyecto = arguments?.getString("nombre_proyecto") ?: "Proyecto"

        // 2. Inicialización de vistas
        txtNombreProyecto = view.findViewById(R.id.txtDetalleNombreProyecto)
        txtNombreEncargado = view.findViewById(R.id.txtNombreEncargado)
        spEstado = view.findViewById(R.id.spDetalleEstado)
        txtNombreProyecto.text = nombreProyecto

        // 3. Spinner de Estados
        val estadosArray = arrayOf("Iniciado", "En proceso", "Finalizado")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estadosArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = adapter

        // 4. Botones
        val btnElegirEncargado = view.findViewById<Button>(R.id.btnElegirEncargado)
        val btnAgregarMateriales = view.findViewById<Button>(R.id.btnAgregarMateriales)
        val btnAgregarCotizacion = view.findViewById<Button>(R.id.btnAgregarCotizacion)
        val btnComparar = view.findViewById<Button>(R.id.btnCompararCotizaciones)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarDetalles)

        // ID del contenedor dinámico
        val idContenedor = (view.parent as ViewGroup).id

        // --- ACCIONES DIÁLOGOS (.show) ---
        btnElegirEncargado.setOnClickListener {
            SeleccionarEncargadoDialog(idProyectoActual).show(childFragmentManager, "SeleccionarEncargadoDialog")
        }

        btnAgregarMateriales.setOnClickListener {
            // Este es Dialog, usa .show
            AgregarMaterialDialog(idProyectoActual).show(childFragmentManager, "AgregarMaterialDialog")
        }

        // --- ACCIONES NAVEGACIÓN FRAGMENT (.replace) ---
        btnAgregarCotizacion.setOnClickListener {
            // Este es Fragment, usa .replace
            val fragmentCotizacion = AgregarCotizacionFragment()
            val bundle = Bundle()
            bundle.putInt("id_proyecto", idProyectoActual)
            fragmentCotizacion.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(idContenedor, fragmentCotizacion)
                .addToBackStack(null)
                .commit()
        }

        btnComparar.setOnClickListener {
            val fragmentComparar = CompararCotizacionesFragment()
            val bundle = Bundle()
            bundle.putInt("id_proyecto", idProyectoActual)
            fragmentComparar.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(idContenedor, fragmentComparar)
                .addToBackStack(null)
                .commit()
        }

        btnGuardar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        actualizarDatos()
    }

    private fun actualizarDatos() {
        val db = DatabaseHelper(requireContext())
        val idUsuario = db.obtenerIdUsuarioDelProyecto(idProyectoActual)
        txtNombreEncargado.text = if (idUsuario > 1) db.obtenerNombreEncargadoPorId(idUsuario) else "Sin asignar (Elija un encargado)"
    }

    override fun onEncargadoElegido(nombre: String) {
        actualizarDatos()
    }

    override fun onResume() {
        super.onResume()
        actualizarDatos()
    }

}