package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class EditarProveedorFragment : Fragment(R.layout.fragment_editar_proveedor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Eliminamos la referencia a btnBackEditarProveedor
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarEditProveedor)

        // Mantenemos solo el botón de cancelar para regresar
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}