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

class SeleccionarProveedorDialog(val onProveedorSeleccionado: (String) -> Unit) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_seleccionar_proveedor, container, false)
    }

    override fun onStart() {
        super.onStart()
        val metrics = resources.displayMetrics
        dialog?.window?.setLayout((metrics.widthPixels * 0.90f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgProveedores = view.findViewById<RadioGroup>(R.id.rgProveedores)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarProveedor)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarProveedor)

        btnCancelar.setOnClickListener { dismiss() }

        btnConfirmar.setOnClickListener {
            val idSeleccionado = rgProveedores.checkedRadioButtonId
            if (idSeleccionado == -1) {
                Toast.makeText(context, "Por favor, seleccione un proveedor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rbSeleccionado = view.findViewById<RadioButton>(idSeleccionado)

            // Extrae únicamente la primera línea con el nombre de la empresa (ej: "Construsal S.A. de C.V.")
            val nombreLimpio = rbSeleccionado.text.toString().split("\n")[0].replace("• ", "").trim()

            onProveedorSeleccionado(nombreLimpio) // Ejecuta el retorno hacia el fragmento principal
            dismiss()
        }
    }
}