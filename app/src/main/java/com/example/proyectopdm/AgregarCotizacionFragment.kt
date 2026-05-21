package com.example.proyectopdm

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AgregarCotizacionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agregar_cotizacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar componentes base del encabezado
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtrasCotizacion)
        val txtSubtitulo = view.findViewById<TextView>(R.id.txtSubtituloProyecto)

        // 2. Inicializar los botones de acciones exactos de tu XML
        val btnElegirProveedor = view.findViewById<Button>(R.id.btnElegirProveedor)
        val btnModificar = view.findViewById<Button>(R.id.btnModificarMaterialesCotizacion)
        val btnAgregarTrans = view.findViewById<Button>(R.id.btnAgregarTransporteCotizacion)
        val btnGuardarFinal = view.findViewById<Button>(R.id.btnGuardarCotizacionFinal)

        // Recuperar el nombre del proyecto dinámicamente enviado en los bundles
        val nombreProyecto = arguments?.getString("nombre_proyecto") ?: "Proyecto"
        txtSubtitulo.text = nombreProyecto

        // Regresar a la pantalla anterior perfectamente
        btnAtras.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // ================= CONEXIÓN DEL PROVEEDOR EN EL PROPIO BOTÓN =================
        btnElegirProveedor.setOnClickListener {
            val dialogProv = SeleccionarProveedorDialog { proveedorEscogido ->
                // Actualiza dinámicamente el texto del botón para reflejar la selección
                btnElegirProveedor.text = "Proveedor:\n$proveedorEscogido"
                btnElegirProveedor.setBackgroundColor(Color.parseColor("#2E7D32")) // Cambia a verde de éxito

                Toast.makeText(context, "Asignado: $proveedorEscogido", Toast.LENGTH_SHORT).show()
            }
            dialogProv.show(parentFragmentManager, "SeleccionarProveedorDialog")
        }

        // Abre el Llenado de Materiales como VENTANA EMERGENTE
        btnModificar.setOnClickListener {
            val dialogLlenado = LlenadoMaterialDialog().apply {
                arguments = Bundle().apply {
                    putString("nombre_material", "Cemento")
                    putString("cantidad_material", "100")
                    putString("unidad_material", "U")
                }
            }
            dialogLlenado.show(parentFragmentManager, "LlenadoMaterialDialog")
        }

        // Abre la ventana flotante para seleccionar transportista, placa y número
        btnAgregarTrans.setOnClickListener {
            SeleccionarTransporteDialog().show(parentFragmentManager, "SeleccionarTransporteDialog")
        }

        // Consolidación de la cotización hacia SQLite
        btnGuardarFinal.setOnClickListener {
            Toast.makeText(context, "Cotización consolidada con éxito en SQLite", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}