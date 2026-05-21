package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnIngresar = view.findViewById<MaterialButton>(R.id.btn_ingresar)

        btnIngresar.setOnClickListener {
            // === PASAMOS DIRECTO AL INICIO ===

            val mainActivity = activity as MainActivity
            mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")

            // Un pequeño mensaje flotante de bienvenida
            Toast.makeText(requireContext(), "¡Bienvenido Usuario a COTMAN!", Toast.LENGTH_SHORT).show()
        }

        //Texto de "Olvidé mi contraseña" (En mantenimiento)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tv_forgot_password)

        tvForgotPassword.setOnClickListener {
            // Muestra el mensaje flotante
            Toast.makeText(requireContext(), "Función en proceso de creación", Toast.LENGTH_SHORT).show()
        }

        val tvRegister = view.findViewById<TextView>(R.id.tv_register)

        tvRegister.setOnClickListener {
            // Transición a la pantalla de Registro
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarUsuarioFragment())
                .addToBackStack(null) // Permite usar el botón físico de "Atrás" del teléfono
                .commit()
        }
    }
}