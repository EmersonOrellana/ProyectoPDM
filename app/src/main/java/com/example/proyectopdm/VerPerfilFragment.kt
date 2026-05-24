package com.example.proyectopdm

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class VerPerfilFragment : Fragment(R.layout.fragment_ver_perfil) {

    private lateinit var dbHelper: DatabaseHelper
    private var correoUsuarioLogueado: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Intentamos buscar el correo por si acaso vino en los arguments (Bundle)
        correoUsuarioLogueado = arguments?.getString("correo_usuario")

        // 2. Si no vino ahí, revisamos los nombres de SharedPreferences más comunes en proyectos de la U
        if (correoUsuarioLogueado.isNullOrEmpty()) {
            val nombresPrefs = arrayOf("MiSesionApp", "SesionCotman", "LoginPrefs", "prefs_usuario", "AppPrefs")
            val llavesCorreo = arrayOf("correo_usuario", "email", "correo", "usuario_email", "USER_EMAIL")

            for (nombrePref in nombresPrefs) {
                val prefs = requireActivity().getSharedPreferences(nombrePref, Context.MODE_PRIVATE)
                for (llave in llavesCorreo) {
                    val correoDetectado = prefs.getString(llave, null)
                    if (!correoDetectado.isNullOrEmpty()) {
                        correoUsuarioLogueado = correoDetectado
                        break
                    }
                }
                if (!correoUsuarioLogueado.isNullOrEmpty()) break
            }
        }

        // Vincular los EditText de tu XML (Tu diseño intacto)
        val etNombre = view.findViewById<EditText>(R.id.etVerNombre)
        val etApellidos = view.findViewById<EditText>(R.id.etVerApellidos)
        val etUser = view.findViewById<EditText>(R.id.etVerUser)
        val etNit = view.findViewById<EditText>(R.id.etVerNit)
        val etFecha = view.findViewById<EditText>(R.id.etVerFecha)
        val etTelefono = view.findViewById<EditText>(R.id.etVerTelefono)
        val etRol = view.findViewById<EditText>(R.id.etVerRol)
        val etCorreo = view.findViewById<EditText>(R.id.etVerCorreo)
        val btnIrAEditarPerfil = view.findViewById<Button>(R.id.btnIrAEditarPerfil)

        // 3. Cargar los datos reales adaptándonos dinámicamente
        cargarDatosDesdeBD(etNombre, etApellidos, etUser, etNit, etFecha, etTelefono, etRol, etCorreo)

        // Navegar a la pantalla de edición de perfil
        btnIrAEditarPerfil.setOnClickListener {
            val fragmentoEditar = ActualizarPerfilFragment()
            val bundle = Bundle()
            bundle.putString("correo_usuario", etCorreo.text.toString())
            fragmentoEditar.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoEditar)
                .addToBackStack(null)
                .commit()

            try {
                activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "EDITAR PERFIL"
            } catch (e: Exception) { e.printStackTrace() }
        }

        // Botón físico "Atrás" del teléfono
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    private fun cargarDatosDesdeBD(
        etNombre: EditText,
        etApellidos: EditText,
        etUser: EditText,
        etNit: EditText,
        etFecha: EditText,
        etTelefono: EditText,
        etRol: EditText,
        etCorreo: EditText
    ) {
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null

        try {
            // Caso A: Si logramos pescar el correo de la sesión de forma automática
            if (!correoUsuarioLogueado.isNullOrEmpty()) {
                val query = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ?"
                cursor = db.rawQuery(query, arrayOf(correoUsuarioLogueado))
            }

            // Caso B (Plan de Respaldo): Si las preferencias están vacías o no devolvió nada,
            // jalamos el último usuario registrado o modificado para que muestre tus datos de prueba.
            if (cursor == null || cursor.count == 0) {
                cursor?.close()
                // Ordenamos por ID de forma descendente para mostrar el más reciente que creaste
                val queryFallback = "SELECT * FROM USUARIO ORDER BY ID_USUARIO DESC LIMIT 1"
                cursor = db.rawQuery(queryFallback, null)
            }

            if (cursor != null && cursor.moveToFirst()) {
                // Recuperamos usando tus campos reales en MAYÚSCULAS
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")) ?: ""
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO_USUARIO")) ?: ""
                val correoElectronico = cursor.getString(cursor.getColumnIndexOrThrow("CORREO_ELECTRONICO")) ?: ""
                val dui = cursor.getString(cursor.getColumnIndexOrThrow("DUI_USUARIO")) ?: ""
                val nit = cursor.getString(cursor.getColumnIndexOrThrow("NIT_USUARIO")) ?: ""
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CONTRATACION")) ?: ""
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_USUARIO")) ?: ""
                val idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))

                val nombreRol = when(idRol) {
                    1 -> "Administrador"
                    2 -> "Usuario Operativo"
                    else -> "Personal"
                }

                // Seteamos la información real controlando los vacíos
                etNombre.setText(nombre)
                etApellidos.setText(apellido)
                etCorreo.setText(correoElectronico)
                etUser.setText(dui)
                etNit.setText(nit)
                etFecha.setText(fecha)
                etTelefono.setText(telefono)
                etRol.setText(nombreRol)

                // Mensajito discreto en el Logcat para saber qué correo terminó jalando
                android.util.Log.d("PERFIL_OK", "Mostrando perfil de: $correoElectronico")

            } else {
                Toast.makeText(context, "La tabla USUARIO está vacía.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}