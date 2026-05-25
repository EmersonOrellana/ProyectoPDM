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

class RecuperarPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recuperar_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etCorreo = view.findViewById<EditText>(R.id.et_correo_recuperacion)
        val etNuevaPass = view.findViewById<EditText>(R.id.et_nueva_password)
        val etConfirmarPass = view.findViewById<EditText>(R.id.et_confirmar_password)
        val btnActualizar = view.findViewById<MaterialButton>(R.id.btn_actualizar_password)
        val tvVolver = view.findViewById<TextView>(R.id.tv_volver_login)

        // Botón para actualizar contraseña
        btnActualizar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val nuevaPass = etNuevaPass.text.toString().trim()
            val confirmarPass = etConfirmarPass.text.toString().trim()

            if (correo.isEmpty() || nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
                Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaPass != confirmarPass) {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //LÓGICA DE BASE DE DATOS
            val dbHelper = DatabaseHelper(requireContext())
            val seActualizo = dbHelper.actualizarPassword(correo, nuevaPass)

            if (seActualizo) {
                Toast.makeText(context, "Contraseña actualizada exitosamente", Toast.LENGTH_LONG).show()
                // Regresamos al Login si todo salió bien
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Error: No se encontró ese correo registrado", Toast.LENGTH_LONG).show()
            }
        }

        // Texto para cancelar y volver
        tvVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}