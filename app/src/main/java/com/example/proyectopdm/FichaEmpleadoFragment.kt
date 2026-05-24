package com.example.proyectopdm

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FichaEmpleadoFragment : Fragment(R.layout.fragment_ficha_empleado) {

    private lateinit var dbHelper: DatabaseHelper
    private var correoEmpleado: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Recuperamos la credencial enviada por la lista
        correoEmpleado = arguments?.getString("correo_usuario")

        // 2. Vinculamos los TextView con los IDs del XML nuevo
        val tvFichaTitulo = view.findViewById<TextView>(R.id.tvFichaTitulo)
        val tvNombreCard = view.findViewById<TextView>(R.id.tvFichaNombreCard)
        val tvRolCard = view.findViewById<TextView>(R.id.tvFichaRolCard)
        val tvNit = view.findViewById<TextView>(R.id.tvFichaNit)
        val tvFecha = view.findViewById<TextView>(R.id.tvFichaFecha)
        val tvDui = view.findViewById<TextView>(R.id.tvFichaDui)
        val tvTelefono = view.findViewById<TextView>(R.id.tvFichaTelefono)
        val tvCorreo = view.findViewById<TextView>(R.id.tvFichaCorreo)
        val tvUsuario = view.findViewById<TextView>(R.id.tvFichaUsuario)

        val btnVerProyectos = view.findViewById<Button>(R.id.btnVerProyectos)

        // 3. Jalar la data desde la base de datos
        if (!correoEmpleado.isNullOrEmpty()) {
            consultarYMostrarEmpleado(correoEmpleado!!, tvFichaTitulo, tvNombreCard, tvRolCard, tvNit, tvFecha, tvDui, tvTelefono, tvCorreo, tvUsuario)
        } else {
            Toast.makeText(context, "Error: No se recibió credencial de consulta", Toast.LENGTH_SHORT).show()
        }

        // 4. Navegar al fragmento de proyectos asignados
        btnVerProyectos.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("correo_usuario", correoEmpleado)

            val fragProyectos = ProyectosAsignadosFragment()
            fragProyectos.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragProyectos)
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Proyectos Asignados"
        }

        // 5. Soporte nativo para regresar de forma limpia
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, PersonalFragment())
                        .commit()

                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
                }
            }
        )
    }

    // 🔍 SELECCIÓN SIN ERRORES EN TU TABLA LOCAL
    private fun consultarYMostrarEmpleado(
        correoFiltro: String,
        tvTitulo: TextView,
        tvNombre: TextView,
        tvRol: TextView,
        tvNitText: TextView,
        tvFechaText: TextView,
        tvDuiText: TextView,
        tvTelText: TextView,
        tvCorreoText: TextView,
        tvUserText: TextView
    ) {
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            val query = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ?"
            cursor = db.rawQuery(query, arrayOf(correoFiltro))

            if (cursor != null && cursor.moveToFirst()) {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")) ?: ""
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO_USUARIO")) ?: ""
                val nombreCompleto = "$nombre $apellido".trim()

                tvTitulo.text = nombreCompleto
                tvNombre.text = nombreCompleto

                val idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                tvRol.text = if (idRol == 1) "Administrador" else "Usuario Operativo"

                val nitVal = cursor.getString(cursor.getColumnIndexOrThrow("NIT_USUARIO")) ?: ""
                tvNitText.text = if (nitVal.isEmpty()) "NIT: No asignado" else "NIT: $nitVal"

                val duiVal = cursor.getString(cursor.getColumnIndexOrThrow("DUI_USUARIO")) ?: ""
                tvDuiText.text = if (duiVal.isEmpty()) "No asignado" else duiVal

                val fechaVal = cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CONTRATACION")) ?: ""
                tvFechaText.text = if (fechaVal.isEmpty()) "No registrada" else fechaVal

                val telVal = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_USUARIO")) ?: ""
                tvTelText.text = if (telVal.isEmpty()) "Sin teléfono" else telVal

                tvCorreoText.text = correoFiltro
                tvUserText.text = correoFiltro.substringBefore("@")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al procesar consulta: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}