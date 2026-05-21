package com.example.proyectopdm

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.Calendar

class CrearProyectoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crear_proyecto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtFecha = view.findViewById<EditText>(R.id.edtFormFecha)
        val btnGuardar = view.findViewById<Button>(R.id.btnFormGuardar)
        val btnCancelar = view.findViewById<Button>(R.id.btnFormCancelar)
        val spDepto = view.findViewById<Spinner>(R.id.spFormDepartamento)
        val spMuni = view.findViewById<Spinner>(R.id.spFormMunicipio)

        // 1. Datos de El Salvador (Simplificados para ejemplo)
        val departamentos = listOf("San Salvador", "La Libertad", "Santa Ana")
        val municipios = mapOf(
            "San Salvador" to listOf("San Salvador", "Mejicanos", "Soyapango"),
            "La Libertad" to listOf("Santa Tecla", "Antiguo Cuscatlán", "Colón"),
            "Santa Ana" to listOf("Santa Ana", "Chalchuapa")
        )

        // 2. Configurar Spinner Departamentos
        val adapterDepto = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, departamentos)
        spDepto.adapter = adapterDepto

        // 3. Lógica para actualizar Municipios al cambiar Departamento
        spDepto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val deptoSeleccionado = departamentos[position]
                val listaMunicipios = municipios[deptoSeleccionado] ?: emptyList()
                val adapterMuni = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaMunicipios)
                spMuni.adapter = adapterMuni
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnCancelar.setOnClickListener { parentFragmentManager.popBackStack() }

        edtFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                edtFecha.setText(String.format("%02d/%02d/%d", d, m + 1, y))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnGuardar.setOnClickListener {
            Toast.makeText(context, "Proyecto creado en ${spMuni.selectedItem}", Toast.LENGTH_SHORT).show()
        }
    }
}