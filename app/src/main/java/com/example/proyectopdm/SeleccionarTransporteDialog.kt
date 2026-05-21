package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText

class SeleccionarTransporteDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hace el fondo transparente para que luzcan las esquinas curvas del CardView exterior
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_seleccionar_transporte, container, false)
    }

    override fun onStart() {
        super.onStart()
        // Ajuste matemático del ancho al 90% de la pantalla del dispositivo
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar los componentes nativos mapeados desde el nuevo XML
        val rgTransportistas = view.findViewById<RadioGroup>(R.id.rgTransportistas)

        // CORRECCIÓN CLAVE: Se castea como TextInputEditText para hacer match exacto con Material Design
        val edtCosto = view.findViewById<TextInputEditText>(R.id.edtCostoTransporteDialog)

        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarTransporte)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarTransporte)

        // Evento para cerrar la ventana flotante de inmediato
        btnCancelar.setOnClickListener { dismiss() }

        // Evento para procesar y validar las entradas
        btnConfirmar.setOnClickListener {
            val idSeleccionado = rgTransportistas.checkedRadioButtonId
            val costoText = edtCosto.text.toString().trim()

            // 1. Validar que se haya marcado alguna opción de la lista suelta
            if (idSeleccionado == -1) {
                Toast.makeText(context, "Por favor, seleccione un transportista", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Validar que el campo del flete no esté vacío ni en cero
            val costoDouble = costoText.toDoubleOrNull()
            if (costoText.isEmpty() || costoDouble == null || costoDouble <= 0) {
                Toast.makeText(context, "Ingrese un costo de flete válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Capturar dinámicamente el texto del RadioButton seleccionado
            val rbSeleccionado = view.findViewById<RadioButton>(idSeleccionado)

            // Limpiamos los saltos de línea (\n) del string para que la alerta y la persistencia guarden solo el nombre limpio
            val nombreLimpio = rbSeleccionado.text.toString().split("\n")[0].replace("• ", "").trim()

            // Simulación exitosa (Aquí conectarás el DBHelper o bundle de retorno en la siguiente fase)
            Toast.makeText(context, "$nombreLimpio asignado por $$costoText", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}