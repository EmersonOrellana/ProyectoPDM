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

    // Variable global para guardar la relación ID-Nombre de los municipios cargados
    private var listaMunicipiosActual = listOf<Pair<Int, String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear_proyecto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializar vistas
        val edtNombre = view.findViewById<EditText>(R.id.edtFormNombre)
        val edtFecha = view.findViewById<EditText>(R.id.edtFormFecha)
        val edtDir = view.findViewById<EditText>(R.id.edtFormDireccion)
        val spDepto = view.findViewById<Spinner>(R.id.spFormDepartamento)
        val spMuni = view.findViewById<Spinner>(R.id.spFormMunicipio)
        val btnGuardar = view.findViewById<Button>(R.id.btnFormGuardar)
        val btnCancelar = view.findViewById<Button>(R.id.btnFormCancelar)

        val dbHelper = DatabaseHelper(requireContext())

        // 2. Cargar Departamentos
        val listaDeptos = dbHelper.getAllDepartamentos()
        val nombresDeptos = listaDeptos.map { it.second }

        val adapterDepto = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresDeptos)
        spDepto.adapter = adapterDepto

        // 3. Listener para filtrar municipios
        spDepto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtenemos el ID del depto seleccionado
                val idDeptoSeleccionado = listaDeptos[position].first

                // Obtenemos lista completa de municipios (ID, Nombre)
                listaMunicipiosActual = dbHelper.getMunicipiosByDepto(idDeptoSeleccionado)

                // Llenamos el segundo spinner solo con nombres
                val adapterMuni = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaMunicipiosActual.map { it.second })
                spMuni.adapter = adapterMuni
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 4. Configurar Selector de Fecha
        edtFecha.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                edtFecha.setText(String.format("%02d/%02d/%d", d, m + 1, y))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 5. Lógica de Guardado
        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val fecha = edtFecha.text.toString()
            val direccion = edtDir.text.toString()

            if (nombre.isBlank() || fecha.isBlank() || direccion.isBlank() || listaMunicipiosActual.isEmpty()) {
                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtenemos el ID real del municipio para la llave foránea
            val idMuniSeleccionado = listaMunicipiosActual[spMuni.selectedItemPosition].first

            // Guardamos en la base de datos
            val idGenerado = dbHelper.insertProyecto(nombre, fecha, direccion, idMuniSeleccionado)

            if (idGenerado != -1L) {
                Toast.makeText(context, "Proyecto '$nombre' guardado correctamente", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Error al guardar el proyecto en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}