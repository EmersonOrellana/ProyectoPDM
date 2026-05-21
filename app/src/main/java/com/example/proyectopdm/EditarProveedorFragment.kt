package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class EditarProveedorFragment : Fragment(R.layout.fragment_editar_proveedor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBackEditarProveedor)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditProveedor)

        val regresarAccion = View.OnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnBack.setOnClickListener(regresarAccion)
        btnCancelar.setOnClickListener(regresarAccion)
    }
}