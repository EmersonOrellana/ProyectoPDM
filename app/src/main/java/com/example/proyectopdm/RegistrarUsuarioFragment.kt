package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class RegistrarUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrar_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLogin = view.findViewById<TextView>(R.id.tv_login)
        tvLogin.setOnClickListener {
            // Destruye esta pantalla y regresa al Login
            requireActivity().supportFragmentManager.popBackStack()
        }

        val btnCrearCuenta = view.findViewById<MaterialButton>(R.id.btn_crear_cuenta)
        btnCrearCuenta.setOnClickListener {
            // Muestra un mensaje flotante al usuario
            Toast.makeText(requireContext(), "Función en proceso de creación", Toast.LENGTH_SHORT).show()
        }

    }
}