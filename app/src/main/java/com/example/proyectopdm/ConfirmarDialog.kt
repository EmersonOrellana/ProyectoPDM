package com.example.proyectopdm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

// NOTA: Hereda de DialogFragment() en lugar de Fragment()
class ConfirmarDialog : DialogFragment() {

    private var alConfirmar: (() -> Unit)? = null

    companion object {
        private const val ARG_TITULO = "arg_titulo"
        private const val ARG_BOTON = "arg_boton"

        // Este método fabrica el diálogo con los textos que tú quieras desde fuera
        fun newInstance(titulo: String, textoBoton: String, accion: () -> Unit): ConfirmarDialog {
            val fragment = ConfirmarDialog()
            val args = Bundle().apply {
                putString(ARG_TITULO, titulo)
                putString(ARG_BOTON, textoBoton)
            }
            fragment.arguments = args
            fragment.alConfirmar = accion
            return fragment
        }
    }

    // Igual que un fragment normal, aquí se infla la vista XML
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_confirmar_eliminacion, container, false)
    }

    // Aquí controlas los clics de los componentes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TRUCO: Hace transparente el fondo cuadrado por defecto de Android
        // para que se vean las esquinas redondeadas de tu CardView
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvTitulo = view.findViewById<TextView>(R.id.tvDialogTitulo)
        val btnConfirmar = view.findViewById<Button>(R.id.btnDialogConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.btnDialogCancelar)

        // Asignar los textos dinámicos que pasamos en el newInstance
        tvTitulo.text = arguments?.getString(ARG_TITULO)
        btnConfirmar.text = arguments?.getString(ARG_BOTON)

        // Acción del botón Rojo (Eliminar / Dar de Baja)
        btnConfirmar.setOnClickListener {
            alConfirmar?.invoke() // Ejecuta el código que programaste afuera
            dismiss()             // Cierra el diálogo automáticamente
        }

        // Acción del botón Cancelar
        btnCancelar.setOnClickListener {
            dismiss()             // Solo cierra el diálogo sin hacer nada
        }
    }
}