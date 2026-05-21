package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class LlenadoMaterialDialog : DialogFragment() {

    private lateinit var edtMaterial: EditText
    private lateinit var edtCantidad: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var txtSubtotal: TextView
    private lateinit var spUnidad: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hace el fondo transparente para que se aprecien las esquinas redondeadas del XML
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_llenado_material, container, false)
    }

    // TRUCO CLAVE: Fuerza a la ventana emergente a ocupar el 90% del ancho del emulador
    override fun onStart() {
        super.onStart()
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar componentes
        edtMaterial = view.findViewById(R.id.edtLlenadoNombreMaterial)
        edtCantidad = view.findViewById(R.id.edtLlenadoCantidad)
        edtPrecio = view.findViewById(R.id.edtLlenadoPrecioUnitario)
        txtSubtotal = view.findViewById(R.id.txtLlenadoSubtotal)
        spUnidad = view.findViewById(R.id.spLlenadoUnidad)
        val btnConfirmar = view.findViewById<Button>(R.id.btnLlenadoConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.btnLlenadoCancelar)

        // 2. Recuperar el material de ejemplo que mandamos al abrir el diálogo
        val materialSeleccionado = arguments?.getString("nombre_material") ?: "Material"
        val cantidadInicial = arguments?.getString("cantidad_material") ?: ""
        val unidadInicial = arguments?.getString("unidad_material") ?: "U"

        edtMaterial.setText(materialSeleccionado)
        edtCantidad.setText(cantidadInicial)

        // 3. Poblar el Spinner de Unidades
        val unidades = arrayOf("U", "qq", "m3", "KG", "Mtr", "Bolsa")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidad.adapter = adapter

        val posUnidad = unidades.indexOf(unidadInicial)
        if (posUnidad != -1) spUnidad.setSelection(posUnidad)

        // 4. Calculador automático de Subtotal en tiempo real
        val calculadorWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calcularSubtotal()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        edtCantidad.addTextChangedListener(calculadorWatcher)
        edtPrecio.addTextChangedListener(calculadorWatcher)

        // 5. Configurar acciones de botones (dismiss cierra el Dialog automáticamente)
        btnCancelar.setOnClickListener { dismiss() }

        btnConfirmar.setOnClickListener {
            val total = txtSubtotal.text.toString()
            Toast.makeText(context, "$materialSeleccionado actualizado con $total", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun calcularSubtotal() {
        val cant = edtCantidad.text.toString().toDoubleOrNull() ?: 0.0
        val precio = edtPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val subtotal = cant * precio

        if (subtotal > 0) {
            txtSubtotal.text = String.format("SUBTOTAL: $%.2f", subtotal)
        } else {
            txtSubtotal.text = "SUBTOTAL: $---,--"
        }
    }
}