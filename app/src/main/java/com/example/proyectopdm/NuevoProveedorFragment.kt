package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class NuevoProveedorFragment : Fragment(R.layout.fragment_nuevo_proveedor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackProveedor)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarProveedor)

        // Acción para regresar a la pantalla anterior
        val regresarAccion = View.OnClickListener {
            // Regresa al fragmento anterior en el historial
            parentFragmentManager.popBackStack()
        }

        btnBack.setOnClickListener(regresarAccion)
        btnCancelar.setOnClickListener(regresarAccion)
    }
}