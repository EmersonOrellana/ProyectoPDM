package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AjustesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Asegúrate de que el XML se llame fragment_ajustes
        return inflater.inflate(R.layout.fragment_ajustes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar Tarjetas (IDs según tu XML anterior)
        val cardMatriz = view.findViewById<MaterialCardView>(R.id.cardMatrizAcceso)
        val cardRoles = view.findViewById<MaterialCardView>(R.id.cardGestionRoles)
        val cardMedida = view.findViewById<MaterialCardView>(R.id.cardUnidadMedida)
        val cardConversion = view.findViewById<MaterialCardView>(R.id.cardReglasConversion)
        val cardCategorias = view.findViewById<MaterialCardView>(R.id.cardCategorias)

        // 2. Inicializar Botones (IDs según tu XML anterior)
        val btnPerfil = view.findViewById<MaterialButton>(R.id.btnVerPerfilAjustes)
        val btnSalir = view.findViewById<MaterialButton>(R.id.btnCerrarSesionAjustes)

        // EVENTOS DE CLIC
        cardMatriz.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, MatrizAccesoFragment())
                .addToBackStack("Ajustes")
                .commit()
        }

        // ====== AQUÍ ACTUALIZAMOS LA NAVEGACIÓN DE ROLES ======
        cardRoles.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RolesListaFragment())
                .addToBackStack("Ajustes") // Vital para mantener el historial
                .commit()

            // Actualizar título al diseño principal
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }
        // =======================================================

        cardMedida.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, UnidadesMedidaFragment())
                .addToBackStack(null)
                .commit()
        }

        cardCategorias.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, CategoriasFragment())
                .addToBackStack("Ajustes") // Vital para poder volver atrás
                .commit()

            // Actualizar título
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "CATEGORÍAS"
        }

        cardConversion.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, ReglasConversionFragment()) // Asegúrate de crear este fragmento
                .addToBackStack("Ajustes")
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "REGLAS DE CONVERSIÓN"
        }



        btnPerfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, VerPerfilFragment())
                .commit()
        }

        // BOTÓN CERRAR SESIÓN
        btnSalir.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cerrar Sesión")
            builder.setMessage("¿Estás seguro que deseas cerrar sesión?")
            builder.setPositiveButton("Sí") { _, _ ->
                val mainActivity = activity as? MainActivity
                mainActivity?.let {
                    it.findViewById<View>(R.id.incluir_cabecera)?.visibility = View.GONE
                    it.findViewById<View>(R.id.incluir_barra_navegacion)?.visibility = View.GONE
                    it.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    it.supportFragmentManager.beginTransaction()
                        .replace(R.id.content_container, LoginFragment())
                        .commit()
                }
            }
            builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }
    }
}