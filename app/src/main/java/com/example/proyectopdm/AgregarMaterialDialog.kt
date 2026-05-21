package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AgregarMaterialDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fondo transparente para aplicar los bordes curvados del CardView de forma limpia
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_agregar_material, container, false)
    }

    override fun onStart() {
        super.onStart()
        // Ajuste dinámico del tamaño para que no salga angosto en pantallas medianas/grandes
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val targetWidth = (width * 0.90f).toInt()
        dialog?.window?.setLayout(targetWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicialización de componentes
        val edtBuscar = view.findViewById<EditText>(R.id.edtBuscarMaterialDialog)
        val edtCantidad = view.findViewById<EditText>(R.id.edtDialogCantidad)
        val spUnidad = view.findViewById<Spinner>(R.id.spDialogUnidad)
        val rgMateriales = view.findViewById<RadioGroup>(R.id.rgMaterialesResultado)
        val btnAgregar = view.findViewById<Button>(R.id.btnDialogAgregarMaterial)
        val btnCancelar = view.findViewById<Button>(R.id.btnDialogCancelarMaterial)

        // 2. Poblar el Spinner de Unidades de medida utilizadas en El Salvador
        val unidadesArray = arrayOf("Seleccione", "U", "QQ", "M3", "KG", "Mtr", "Bolsa")
        val adapterUnidades = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unidadesArray)
        adapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUnidad.adapter = adapterUnidades

        // 3. Listener del botón cancelar
        btnCancelar.setOnClickListener {
            dismiss()
        }

        // 4. Listener para capturar la selección e inserción simulada
        btnAgregar.setOnClickListener {
            val idSeleccionado = rgMateriales.checkedRadioButtonId
            val cantidadText = edtCantidad.text.toString()
            val unidadSeleccionada = spUnidad.selectedItem.toString()

            if (idSeleccionado == -1) {
                Toast.makeText(context, "Por favor, seleccione un material", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cantidadText.isEmpty() || cantidadText.toDouble() <= 0) {
                Toast.makeText(context, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (unidadSeleccionada == "Seleccione") {
                Toast.makeText(context, "Seleccione una unidad de medida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulación exitosa antes de mapear el DBHelper definitivo
            Toast.makeText(context, "Material añadido satisfactoriamente", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}