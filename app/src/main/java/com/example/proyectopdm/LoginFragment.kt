package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enlazamos este código con el diseño XML
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buscamos el botón de ingresar
        val btnIngresar = view.findViewById<MaterialButton>(R.id.btn_ingresar)

        btnIngresar.setOnClickListener {
            // Cuando hacemos clic, le decimos a la Actividad Principal que cambie a la pantalla de Inicio
            val mainActivity = activity as MainActivity
            mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
        }
    }
}