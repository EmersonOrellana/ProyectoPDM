package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Importación clave para el diálogo
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AjustesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ajustes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Tarjetas de Opciones
        val cardMatriz = view.findViewById<MaterialCardView>(R.id.cardMatrizAcceso)
        val cardRoles = view.findViewById<MaterialCardView>(R.id.cardGestionRoles)
        val cardMedida = view.findViewById<MaterialCardView>(R.id.cardUnidadMedida)
        val cardConversion = view.findViewById<MaterialCardView>(R.id.cardReglasConversion)
        val cardCategorias = view.findViewById<MaterialCardView>(R.id.cardCategorias)

        // Inicializar Botones Inferiores
        val btnPerfil = view.findViewById<MaterialButton>(R.id.btnVerPerfilAjustes)
        val btnSalir = view.findViewById<MaterialButton>(R.id.btnCerrarSesionAjustes)

        // ================= EVENTOS DE CLIC =================
        cardMatriz.setOnClickListener {
            Toast.makeText(context, "Módulo: Matriz de Acceso y Permisos", Toast.LENGTH_SHORT).show()
        }

        cardRoles.setOnClickListener {
            Toast.makeText(context, "Módulo: Gestión y Creación de Roles", Toast.LENGTH_SHORT).show()
        }

        cardMedida.setOnClickListener {
            Toast.makeText(context, "Catálogo: Unidades de Medida", Toast.LENGTH_SHORT).show()
        }

        cardConversion.setOnClickListener {
            Toast.makeText(context, "Catálogo: Reglas de Conversión", Toast.LENGTH_SHORT).show()
        }

        cardCategorias.setOnClickListener {
            Toast.makeText(context, "Catálogo: Categorías de Materiales", Toast.LENGTH_SHORT).show()
        }

        btnPerfil.setOnClickListener {
            Toast.makeText(context, "Abriendo Perfil del Usuario", Toast.LENGTH_SHORT).show()
        }

        // ================= BOTÓN CERRAR SESIÓN CON CONFIRMACIÓN =================
        btnSalir.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cerrar Sesión")
            builder.setMessage("¿Estás seguro que deseas cerrar sesión?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                Toast.makeText(context, "Cerrando sesión en COTMAN...", Toast.LENGTH_SHORT).show()

                // Ejecuta el cambio de pantalla al Login y oculta el menú inferior
                (activity as? MainActivity)?.cambiarPantalla(
                    LoginFragment(),
                    0,
                    "Iniciar Sesión"
                )
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }
    }
}