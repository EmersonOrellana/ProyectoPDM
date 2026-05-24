package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

        // Enlazamos los componentes usando los IDs
        val etNombres = view.findViewById<EditText>(R.id.et_nombres)
        val etApellidos = view.findViewById<EditText>(R.id.et_apellidos)
        val etCorreo = view.findViewById<EditText>(R.id.et_correo_reg)
        val etPassword = view.findViewById<EditText>(R.id.et_contrasena_reg)
        val etConfirmPassword = view.findViewById<EditText>(R.id.et_confirmar_contrasena)
        val btnCrearCuenta = view.findViewById<MaterialButton>(R.id.btn_crear_cuenta)
        val tvLogin = view.findViewById<TextView>(R.id.tv_login)

        btnCrearCuenta.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validaciones
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamada a la base de datos (DatabaseHelper)
            val dbHelper = DatabaseHelper(requireContext())
            val exito = dbHelper.registrarUsuario(nombres, apellidos, correo, password)

            if (exito) {
                Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
                // Regresa al login
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Error al registrar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}