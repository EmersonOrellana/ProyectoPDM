package com.example.proyectopdm

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class CompararCotizacionesFragment : Fragment() {

    private var cotizacionSeleccionada: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comparar_cotizaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar botón de regreso del encabezado y buscador
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtrasComparar)
        val edtBuscar = view.findViewById<EditText>(R.id.edtBuscarCotizaciones)

        // 2. Inicializar tarjetas interactivas
        val cardCotizacion1 = view.findViewById<MaterialCardView>(R.id.cardCotizacion1)
        val cardCotizacion2 = view.findViewById<MaterialCardView>(R.id.cardCotizacion2)

        // 3. Inicializar botones de acciones internas
        val btnVerDetalle1 = view.findViewById<MaterialButton>(R.id.btnVerDetalle1)
        val btnEditar1 = view.findViewById<MaterialButton>(R.id.btnEditar1)
        val btnEliminar1 = view.findViewById<MaterialButton>(R.id.btnEliminar1)

        val btnVerDetalle2 = view.findViewById<MaterialButton>(R.id.btnVerDetalle2)
        val btnEditar2 = view.findViewById<MaterialButton>(R.id.btnEditar2)
        val btnEliminar2 = view.findViewById<MaterialButton>(R.id.btnEliminar2)

        val btnSeleccionarCotizacionFinal = view.findViewById<MaterialButton>(R.id.btnSeleccionarCotizacionFinal)

        // ================= ACCIÓN DEL BOTÓN ATRÁS (CONFIGURADO AL 100%) =================
        btnAtras.setOnClickListener {
            parentFragmentManager.popBackStack() // Saca este fragmento de la pila y regresa a la pestaña previa
        }

        // ================= SELECCIÓN DE LAS COTIZACIONES =================
        cardCotizacion1.setOnClickListener {
            cotizacionSeleccionada = "Cotización 1 (Proveedor A)"
            cardCotizacion1.strokeColor = android.graphics.Color.parseColor("#1565C0")
            cardCotizacion1.strokeWidth = 6
            cardCotizacion2.strokeWidth = 0
            Toast.makeText(context, "Marcaste: Cotización 1", Toast.LENGTH_SHORT).show()
        }

        cardCotizacion2.setOnClickListener {
            cotizacionSeleccionada = "Cotización 2 (Proveedor B)"
            cardCotizacion2.strokeColor = android.graphics.Color.parseColor("#1565C0")
            cardCotizacion2.strokeWidth = 6
            cardCotizacion1.strokeWidth = 0
            Toast.makeText(context, "Marcaste: Cotización 2", Toast.LENGTH_SHORT).show()
        }

        // ================= VENTANA EMERGENTE DE DETALLES =================
        btnVerDetalle1.setOnClickListener {
            val dialogDetalle = DetalleCotizacionDialog().apply {
                arguments = Bundle().apply {
                    putString("num_cotizacion", "Cotización 1")
                    putString("proveedor", "Proveedor A")
                    putString("costo_materiales", "1,112.25")
                    putString("costo_transporte", "150.00")
                    putString("costo_total", "1,262.25")
                }
            }
            dialogDetalle.show(parentFragmentManager, "DetailDialog1")
        }

        btnVerDetalle2.setOnClickListener {
            val dialogDetalle = DetalleCotizacionDialog().apply {
                arguments = Bundle().apply {
                    putString("num_cotizacion", "Cotización 2")
                    putString("proveedor", "Proveedor B")
                    putString("costo_materiales", "950.00")
                    putString("costo_transporte", "100.00")
                    putString("costo_total", "1,050.00")
                }
            }
            dialogDetalle.show(parentFragmentManager, "DetailDialog2")
        }

        // ================= DIÁLOGO FLOTANTE DE CONFIRMACIÓN =================
        btnSeleccionarCotizacionFinal.setOnClickListener {
            if (cotizacionSeleccionada == null) {
                Toast.makeText(context, "Por favor, seleccione una cotización de la lista primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmar Selección")
            builder.setMessage("¿Seguro que quiere elegir esa cotización?\n\nSelección: $cotizacionSeleccionada")

            builder.setPositiveButton("Aceptar") { dialog, _ ->
                Toast.makeText(context, "$cotizacionSeleccionada elegida con éxito", Toast.LENGTH_LONG).show()
                dialog.dismiss()
                parentFragmentManager.popBackStack()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }

        // Acciones auxiliares de control
        btnEditar1.setOnClickListener { Toast.makeText(context, "Editar Cotización 1", Toast.LENGTH_SHORT).show() }
        btnEliminar1.setOnClickListener { Toast.makeText(context, "Eliminar Cotización 1", Toast.LENGTH_SHORT).show() }
        btnEditar2.setOnClickListener { Toast.makeText(context, "Editar Cotización 2", Toast.LENGTH_SHORT).show() }
        btnEliminar2.setOnClickListener { Toast.makeText(context, "Eliminar Cotización 2", Toast.LENGTH_SHORT).show() }
    }
}