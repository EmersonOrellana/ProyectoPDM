package com.example.proyectopdm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class FichaTransportistaFragment : Fragment() {

    private var transportista: Transportista? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ficha_transportista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Capturar el objeto transportista enviado desde la lista
        transportista = arguments?.getSerializable("transportista") as? Transportista

        // 2. Vincular los componentes dinámicos del XML
        val tvFichaNombre = view.findViewById<TextView>(R.id.tvFichaNombre)
        val tvFichaSubtitulo = view.findViewById<TextView>(R.id.tvFichaSubtitulo)
        val tvFichaPlaca = view.findViewById<TextView>(R.id.tvFichaPlaca)
        val tvFichaTipoLicencia = view.findViewById<TextView>(R.id.tvFichaTipoLicencia)
        val tvFichaLicencia = view.findViewById<TextView>(R.id.tvFichaLicencia)
        val tvFichaTelefono = view.findViewById<TextView>(R.id.tvFichaTelefono)
        val tvFichaCorreo = view.findViewById<TextView>(R.id.tvFichaCorreo)

        // 3. Pintar los datos reales del motorista en la pantalla
        transportista?.let { motorista ->
            tvFichaNombre.text = motorista.nombre
            tvFichaSubtitulo.text = "DUI: ${motorista.dui}" // Lo usamos como identificador rápido arriba

            tvFichaPlaca.text = motorista.placa
            tvFichaTipoLicencia.text = motorista.tipoLicencia
            tvFichaLicencia.text = motorista.licencia
            tvFichaTelefono.text = motorista.telefono
            tvFichaCorreo.text = motorista.correo
        }

        // 4. El botón de editar hereda el mismo transportista
        view.findViewById<Button>(R.id.btnFichaEditarInfo).setOnClickListener {
            val fragmentoEditar = EditarTransportistaFragment()
            val bundle = Bundle()
            bundle.putSerializable("transportista", transportista) // Mandamos los datos
            fragmentoEditar.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, fragmentoEditar)
                .addToBackStack(null)
                .commit()
        }
    }
}