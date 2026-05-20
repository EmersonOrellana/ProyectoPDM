package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class SeleccionarEncargadoDialog : DialogFragment() {

    private var usuarioSeleccionadoTemp: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hace el fondo del diálogo transparente para que se vean los bordes redondeados del CardView
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // CORRECCIÓN 1: Apuntar al XML correcto (el simplificado)
        return inflater.inflate(R.layout.dialog_asignar_encargado, container, false)
    }

    // CORRECCIÓN 2: Sobrescribir onStart para ensanchar la ventana al 90%
    override fun onStart() {
        super.onStart()
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()

        // Le indicamos al Window del Dialog que use el 90% del ancho de la pantalla
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar los componentes
        val edtBuscar = view.findViewById<EditText>(R.id.edtBuscarEncargado)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarSelector)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarSelector)

        // Simulación de filas seleccionables
        val rowPersona1 = view.findViewById<TableRow>(R.id.rowPersona1)
        val rowPersona2 = view.findViewById<TableRow>(R.id.rowPersona2)

        // 2. Configurar eventos de selección simulados (Aquí jalarías datos de SQLite con un Adapter)
        rowPersona1.setOnClickListener {
            deseleccionarFilas(view)
            it.setBackgroundColor(Color.parseColor("#A9C9E8")) // Resalta selección
            usuarioSeleccionadoTemp = "Persona 1 (Ingeniero)"
        }

        rowPersona2.setOnClickListener {
            deseleccionarFilas(view)
            it.setBackgroundColor(Color.parseColor("#A9C9E8")) // Resalta selección
            usuarioSeleccionadoTemp = "Persona 2 (Maestro de obra)"
        }

        // 3. Configurar eventos de los botones principales
        btnCancelar.setOnClickListener {
            dismiss() // Cierra la ventana emergente
        }

        btnConfirmar.setOnClickListener {
            if (usuarioSeleccionadoTemp != null) {
                // Aquí capturarás los datos reales y llamarás a tu DBHelper (SQLite)
                Toast.makeText(context, "$usuarioSeleccionadoTemp asignado correctamente", Toast.LENGTH_SHORT).show()
                dismiss() // Cierra después de guardar
            } else {
                Toast.makeText(context, "Por favor, selecciona un encargado primero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deseleccionarFilas(view: View) {
        // Limpia el fondo de todas las filas para simular selección única
        view.findViewById<TableRow>(R.id.rowPersona1).setBackgroundColor(Color.TRANSPARENT)
        view.findViewById<TableRow>(R.id.rowPersona2).setBackgroundColor(Color.TRANSPARENT)
    }
}