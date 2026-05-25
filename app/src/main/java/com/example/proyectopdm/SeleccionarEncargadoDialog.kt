package com.example.proyectopdm

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

class SeleccionarEncargadoDialog(private val idProyecto: Int) : DialogFragment(R.layout.dialog_asignar_encargado) {

    interface OnEncargadoSeleccionado {
        fun onEncargadoElegido(nombre: String)
    }

    private var filaSeleccionada: TableRow? = null
    private var idSeleccionado: Int = -1
    private var nombreSeleccionado: String = ""

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tableLayout = view.findViewById<TableLayout>(R.id.tableUsuarios)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarSelector)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarSelector)

        // 1. LIMPIEZA TOTAL: Eliminamos cualquier dato fijo o previo del XML
        tableLayout.removeAllViews()

        val dbHelper = DatabaseHelper(requireContext())
        val listaUsuarios: List<Pair<Int, String>> = dbHelper.obtenerUsuarios()

        // 2. CARGA DINÁMICA: Solo datos de la BD
        for (usuario in listaUsuarios) {
            val fila = TableRow(requireContext())
            fila.setPadding(24, 24, 24, 24)

            val tv = TextView(requireContext())
            tv.text = "• ${usuario.second}"
            tv.textSize = 16f
            tv.setTextColor(Color.BLACK)
            fila.addView(tv)

            fila.setOnClickListener {
                filaSeleccionada?.setBackgroundColor(Color.TRANSPARENT)
                fila.setBackgroundColor(Color.parseColor("#E0F7FA"))
                filaSeleccionada = fila

                idSeleccionado = usuario.first
                nombreSeleccionado = usuario.second
            }
            tableLayout.addView(fila)
        }

        btnCancelar.setOnClickListener { dismiss() }

        btnConfirmar.setOnClickListener {
            if (idSeleccionado != -1) {
                // 3. ACTUALIZACIÓN SELECTIVA: Solo guardamos el nuevo ID de usuario
                val db = DatabaseHelper(requireContext())
                val exito = db.actualizarProyectoEncargado(idProyecto, idSeleccionado)

                if (exito) {
                    (parentFragment as? OnEncargadoSeleccionado)?.onEncargadoElegido(nombreSeleccionado)
                    dismiss()
                } else {
                    Toast.makeText(context, "Error al guardar en BD", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Selecciona a alguien primero", Toast.LENGTH_SHORT).show()
            }
        }
    }
}