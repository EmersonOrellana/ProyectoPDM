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

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val btnIngresar = view.findViewById<MaterialButton>(R.id.btn_ingresar)
        val tvRegister = view.findViewById<TextView>(R.id.tv_register)

        btnIngresar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DatabaseHelper(requireContext())
            val credenciales = LoginRequest(email, password)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.instance.loginUsuario(credenciales)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.status == "success") {
                            val usuarioRemoto = response.body()?.usuario

                            // CAMBIO: Protección con "?: " para evitar NullPointerException
                            if (usuarioRemoto != null) {
                                dbHelper.registrarUsuario(
                                    usuarioRemoto.NOMBRE_USUARIO ?: "",
                                    usuarioRemoto.APELLIDO_USUARIO ?: "",
                                    usuarioRemoto.CORREO_ELECTRONICO ?: "",
                                    usuarioRemoto.CONTRASENA ?: ""
                                )
                                Toast.makeText(requireContext(), "¡Bienvenido a COTMAN!", Toast.LENGTH_SHORT).show()
                                val mainActivity = activity as MainActivity
                                mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
                            } else {
                                Toast.makeText(requireContext(), "Error: Datos de usuario incompletos", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Si el servidor falla, probamos con SQLite
                            if (dbHelper.validarUsuario(email, password)) {
                                Toast.makeText(requireContext(), "Bienvenido (Modo Offline)", Toast.LENGTH_SHORT).show()
                                val mainActivity = activity as MainActivity
                                mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
                            } else {
                                Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Fallo total de red, probamos última opción: Local
                        val dbHelperOffline = DatabaseHelper(requireContext())
                        if (dbHelperOffline.validarUsuario(email, password)) {
                            Toast.makeText(requireContext(), "Modo Offline (Sin red)", Toast.LENGTH_SHORT).show()
                            val mainActivity = activity as MainActivity
                            mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
                        } else {
                            Toast.makeText(requireContext(), "Error de red y usuario no encontrado local", Toast.LENGTH_LONG).show()
                        }
                        android.util.Log.e("ERROR_CONEXION", "Fallo: ", e)
                    }
                }
            }
        }

        tvRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarUsuarioFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}