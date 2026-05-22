package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransportistasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transportistas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Botón flotante (+) -> REGISTRAR
        view.findViewById<View>(R.id.fabAgregarTransportista)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarTransportistaFragment())
                .addToBackStack(null)
                .commit()
        }

        // 2. Botón Celeste (Editar) -> EDICIÓN DIRECTA
        view.findViewById<View>(R.id.btnEditarTrans)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, EditarTransportistaFragment())
                .addToBackStack(null)
                .commit()
        }

        // 3. Botón Azul Oscuro (Ver Ficha) -> NUEVA PANTALLA DETALLE
        view.findViewById<View>(R.id.btnVerFicha)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, FichaTransportistaFragment())
                .addToBackStack(null)
                .commit()
        }

        // ─── AGREGADO: BOTÓN ROJO (BAJA) PARA MOSTRAR EL DIÁLOGO REUTILIZADO ───
        view.findViewById<View>(R.id.btnBajaTrans)?.setOnClickListener {
            mostrarDialogoEliminar()
        }
    }
    private fun mostrarDialogoEliminar() {
        // Inflamos el XML reutilizable que ya tienen en el grupo
        val vistaDialogo = layoutInflater.inflate(R.layout.dialog_eliminar, null)

        // Personalizamos los textos dinámicamente para que coincida con tu diseño de Transportistas
        val tvTitulo = vistaDialogo.findViewById<android.widget.TextView>(R.id.tv_titulo_eliminar)
        val btnEliminar = vistaDialogo.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_confirmar_eliminar)
        val btnCancelar = vistaDialogo.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancelar_eliminar)
        val ivIcono = vistaDialogo.findViewById<android.widget.ImageView>(android.R.id.custom) // O búscalo si tiene id asignado

        // Cambiamos los textos al vuelo
        tvTitulo?.text = "Confirmar Eliminación"
        btnEliminar?.text = "Si Eliminar"

        // Construimos el AlertDialog flotante
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(vistaDialogo)
            .setCancelable(true)

        val dialog = builder.create()

        // Hacer que el fondo sea transparente para que respete las esquinas redondeadas del CardView
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Acción del botón Cancelar
        btnCancelar?.setOnClickListener {
            dialog.dismiss()
        }

        // Acción del botón Si Eliminar
        btnEliminar?.setOnClickListener {
            android.widget.Toast.makeText(context, "Transportista dado de baja", android.widget.Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            // Aquí irá la lógica para borrarlo de la lista real más adelante
        }

        dialog.show()
    }
}