package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

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
    }
}