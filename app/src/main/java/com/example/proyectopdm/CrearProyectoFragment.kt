package com.example.proyectopdm

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class CrearProyectoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño de forma tradicional para evitar errores de compatibilidad
        return inflater.inflate(R.layout.fragment_crear_proyecto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos los componentes del diseño XML
        val edtFecha = view.findViewById<EditText>(R.id.edtFormFecha)
        val btnAtras = view.findViewById<ImageButton>(R.id.btnAtrasForm)
        val btnGuardar = view.findViewById<Button>(R.id.btnFormGuardar)
        val btnCancelar = view.findViewById<Button>(R.id.btnFormCancelar)

        // Evento para regresar a la pantalla anterior (InicioProyectosFragment)
        btnAtras.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Evento para el botón Cancelar (también regresa a la lista)
        btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Evento para abrir el calendario de forma limpia al tocar el EditText
        edtFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Formateamos la fecha seleccionada en formato legible DD/MM/YYYY
                    val fechaFormateada = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    edtFecha.setText(fechaFormateada)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        // Evento temporal para simular el guardado en la base de datos de COTMAN
        btnGuardar.setOnClickListener {
            Toast.makeText(context, "Guardando proyecto en SQLite...", Toast.LENGTH_SHORT).show()
            // Aquí enlazaremos tu archivo DBHelper para hacer el insert
        }
    }
}