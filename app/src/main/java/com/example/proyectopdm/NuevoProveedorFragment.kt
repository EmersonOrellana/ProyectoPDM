package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class NuevoProveedorFragment : Fragment(R.layout.fragment_nuevo_proveedor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarProveedor)

        // Al presionar cancelar, simplemente cerramos este fragmento
        // y el sistema regresará automáticamente a la pantalla anterior (Proveedores)
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}