package com.example.proyectopdm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

// Asegúrate que el layout se llame fragment_home o fragment_inicio
class InicioFragment : Fragment(R.layout.fragment_inicio) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Referencias a las tarjetas del diseño
        val cardMateriales = view.findViewById<CardView>(R.id.cardMateriales)
        val tvContador = view.findViewById<TextView>(R.id.tvCountMateriales)

        // Texto dinámico opcional
        tvContador.text = "25 productos"

        // 2. Al dar clic en la tarjeta, saltamos a la pantalla de Materiales
        cardMateriales.setOnClickListener {
            (activity as? MainActivity)?.cambiarPantalla(
                MaterialesFragment(),
                R.id.nav_materiales,
                "MATERIALES"
            )
        }

        // Aquí puedes añadir más tarjetas siguiendo el mismo ejemplo
    }
}