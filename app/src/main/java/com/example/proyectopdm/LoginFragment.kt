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

        // --- INICIO PRUEBA BASE DE DATOS ---
        try {
            // 1. Instanciamos nuestra clase Helper
            val dbHelper = DatabaseHelper(requireContext())

            // 2. Abrimos la base de datos en modo lectura/escritura
            val db = dbHelper.openDatabase()

            // 3. Hacemos una consulta de prueba a la tabla ROL
            val cursor = db.rawQuery("SELECT * FROM ROL", null)

            if (cursor.moveToFirst()) {
                do {
                    // Extraemos el nombre del rol
                    val nombreRol = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL"))
                    // Lo imprimimos en la consola de Android Studio (Logcat)
                    android.util.Log.d("PRUEBA_BD", "Rol encontrado en SQLite: $nombreRol")
                } while (cursor.moveToNext())
            }

            // 4. Siempre cerramos el cursor y la base de datos
            cursor.close()
            db.close()
        } catch (e: Exception) {
            android.util.Log.e("PRUEBA_BD", "Error al leer la base de datos", e)
        }
        // --- FIN PRUEBA BASE DE DATOS ---

        val btnIngresar = view.findViewById<MaterialButton>(R.id.btn_ingresar)
        btnIngresar.setOnClickListener {
            requireView().visibility = View.GONE

            val mainActivity = activity as MainActivity
            mainActivity.cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
            Toast.makeText(requireContext(), "¡Bienvenido a COTMAN!", Toast.LENGTH_SHORT).show()
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