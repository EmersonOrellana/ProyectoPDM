package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class FichaTransportistaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ficha_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón Atrás
        view.findViewById<ImageButton>(R.id.btnAtrasFicha).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // ¡Aquí está el truco! Este botón te manda al MISMO fragment de edición
        view.findViewById<Button>(R.id.btnFichaEditarInfo).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, EditarTransportistaFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}