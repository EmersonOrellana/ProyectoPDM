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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrarUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registrar_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Guardar local (SQLite)
            val dbHelper = DatabaseHelper(requireContext())
            val exitoLocal = dbHelper.registrarUsuario(nombres, apellidos, correo, password)

            if (exitoLocal) {
                // Capturamos el contexto antes de lanzar nada
                val context = requireContext()

                // 2. Intentar guardar remoto (API)
                val usuarioRemoto = UsuarioRemoto(0, 2, nombres, apellidos, correo, password)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitClient.instance.registrarUsuario(usuarioRemoto)
                        withContext(Dispatchers.Main) {
                            if (isAdded) { // Verifica si el fragmento sigue activo
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Sincronizado con Laragon", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error en servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                                requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            if (isAdded) {
                                Toast.makeText(context, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                    }
                }
                Toast.makeText(context, "Cuenta creada localmente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Error al registrar en BD local", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}