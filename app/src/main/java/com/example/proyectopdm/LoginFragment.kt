package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnIngresar = view.findViewById<Button>(R.id.btn_ingresar)

        btnIngresar.setOnClickListener {
            Toast.makeText(context, "Sesión Iniciada con Éxito", Toast.LENGTH_SHORT).show()

            // Al ingresar, cargamos el inicio de proyectos y restauramos el menú inferior
            (activity as? MainActivity)?.cambiarPantalla(
                InicioProyectosFragment(),
                R.id.nav_inicio,
                "COTMAN"
            )
        }
    }
}