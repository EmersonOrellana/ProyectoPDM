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

        try {
            //Instanciamos nuestra clase Helper
            val dbHelper = DatabaseHelper(requireContext())

            //Abrimos la base de datos en modo lectura/escritura
            val db = dbHelper.openDatabase()

            //Hacemos una consulta de prueba a la tabla ROL
            val cursor = db.rawQuery("SELECT * FROM ROL", null)

            if (cursor.moveToFirst()) {
                do {
                    // Extraemos el nombre del rol
                    val nombreRol = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL"))
                    android.util.Log.d("PRUEBA_BD", "Rol encontrado en SQLite: $nombreRol")
                } while (cursor.moveToNext())
            }

            // Siempre cerramos el cursor y la base de datos
            cursor.close()
            db.close()
        } catch (e: Exception) {
            android.util.Log.e("PRUEBA_BD", "Error al leer la base de datos", e)
        }

        val btnIngresar = view.findViewById<MaterialButton>(R.id.btn_ingresar)
        // Ahora usamos los IDs que acabamos de poner en el XML
        val etEmail = view.findViewById<android.widget.EditText>(R.id.et_email)
        val etPassword = view.findViewById<android.widget.EditText>(R.id.et_password)

        btnIngresar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validamos con la base de datos
            val dbHelper = DatabaseHelper(requireContext())
            val esValido = dbHelper.validarUsuario(email, password)

            if (esValido) {
                Toast.makeText(requireContext(), "¡Bienvenido a COTMAN!", Toast.LENGTH_SHORT).show()

                // Ocultamos el login de golpe para evitar el parpadeo de la UI
                requireView().visibility = View.GONE

                val mainActivity = activity as MainActivity
                mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
            } else {
                Toast.makeText(requireContext(), "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }

        //RECUPERAR CONTRASEÑA
        val tvForgotPassword = view.findViewById<TextView>(R.id.tv_forgot_password)

        tvForgotPassword.setOnClickListener {
            // Transición a la pantalla de Recuperar Contraseña
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, RecuperarPasswordFragment())
                .addToBackStack(null) // Permite regresar al Login con el botón del celular
                .commit()
        }

        //REGISTRAR USUARIO
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