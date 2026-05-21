package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DetalleCotizacionDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Transparenta el fondo para que no se vean esquinas grises fuera del CardView
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_detalle_cotizacion, container, false)
    }

    override fun onStart() {
        super.onStart()
        // Ajusta la ventana emergente al 90% del ancho del dispositivo de forma limpia
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar componentes
        val txtCotizacionNum = view.findViewById<TextView>(R.id.txtDetalleDialogNumero)
        val txtProveedor = view.findViewById<TextView>(R.id.txtDetalleDialogProveedor)
        val txtMateriales = view.findViewById<TextView>(R.id.txtDetalleDialogCostoMateriales)
        val txtTransporte = view.findViewById<TextView>(R.id.txtDetalleDialogCostoTransporte)
        val txtTotal = view.findViewById<TextView>(R.id.txtDetalleDialogCostoTotal)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarSeleccionDialog)

        // Recuperar los datos del Bundle enviados desde el listado
        val numero = arguments?.getString("num_cotizacion") ?: "Cotización"
        val prov = arguments?.getString("proveedor") ?: "Desconocido"
        val matCosto = arguments?.getString("costo_materiales") ?: "0.00"
        val transCosto = arguments?.getString("costo_transporte") ?: "0.00"
        val totalCosto = arguments?.getString("costo_total") ?: "0.00"

        // Rellenar la interfaz dinámicamente
        txtCotizacionNum.text = numero
        txtProveedor.text = "Proveedor:  $prov"
        txtMateriales.text = "Costo de Materiales: $  $matCosto"
        txtTransporte.text = "Costo de Transporte: $  $transCosto"
        txtTotal.text = "Costo Total: $  $totalCosto"

        // Clic en Confirmar Cierra el Dialog flotante limpia
        btnConfirmar.setOnClickListener {
            dismiss()
        }
    }
}