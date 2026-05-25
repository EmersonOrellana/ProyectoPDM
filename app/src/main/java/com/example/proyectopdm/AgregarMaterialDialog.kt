package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

class AgregarMaterialDialog(private val idProyecto: Int) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_agregar_material, container, false)
    }

    override fun onStart() {
        super.onStart()
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DatabaseHelper(requireContext())
        val edtCantidad = view.findViewById<EditText>(R.id.edtDialogCantidad)
        val spUnidad = view.findViewById<Spinner>(R.id.spDialogUnidad)
        val rgMateriales = view.findViewById<RadioGroup>(R.id.rgMaterialesResultado)
        val btnAgregar = view.findViewById<Button>(R.id.btnDialogAgregarMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnDialogCancelarMaterial)

        // 1. Cargar materiales dinámicamente
        rgMateriales.removeAllViews() // Aseguramos limpieza
        val materiales = db.obtenerMateriales()
        for (material in materiales) {
            val rb = RadioButton(requireContext())
            rb.id = material.idMaterial
            rb.text = material.nombre
            rgMateriales.addView(rb)
        }

        // 2. Cargar unidades dinámicamente
        val listaUnidades = db.recuperarUnidadesMedida()
        val nombresUnidades = listaUnidades.map { it.nombreUnidad }.toMutableList()
        nombresUnidades.add(0, "Seleccione")

        val adapterUnidades = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresUnidades)
        adapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidad.adapter = adapterUnidades

        // 3. Sincronización automática de unidad al elegir material
        rgMateriales.setOnCheckedChangeListener { _, checkedId ->
            val unidadRelacionada = db.obtenerUnidadPorIdMaterial(checkedId)
            val posicion = adapterUnidades.getPosition(unidadRelacionada)
            if (posicion >= 0) spUnidad.setSelection(posicion)
        }

        // 4. Lógica de botones
        btnCancelar.setOnClickListener { dismiss() }

        btnAgregar.setOnClickListener {
            val idMaterial = rgMateriales.checkedRadioButtonId
            val cantidad = edtCantidad.text.toString()
            val unidad = spUnidad.selectedItem.toString()

            if (idMaterial == -1 || cantidad.isEmpty() || unidad == "Seleccione") {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí realizarías tu inserción:
            // db.registrarMaterialProyecto(idProyecto, idMaterial, cantidad.toDouble(), unidad)

            Toast.makeText(context, "Material guardado correctamente", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}