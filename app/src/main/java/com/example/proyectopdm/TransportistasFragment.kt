package com.example.proyectopdm

import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class TransportistasFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransportistaAdapter
    private lateinit var etBuscar: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transportistas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // 1. Vincular los componentes
        recyclerView = view.findViewById(R.id.recyclerViewTransportistas)
        etBuscar = view.findViewById(R.id.etBuscar)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 2. Cargar los datos iniciales y configurar las acciones de cada botón
        val listaTransportistas = obtenerTransportistasDeBD()
        adapter = TransportistaAdapter(
            listaTransportistas,
            onVerFichaClick = { transportista ->
                // ─── COMPLETO: Acción Ver Ficha pasando el objeto seleccionado ───
                val fragmentoFicha = FichaTransportistaFragment()
                val bundle = Bundle()
                bundle.putSerializable("transportista", transportista)
                fragmentoFicha.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragmentoFicha)
                    .addToBackStack(null)
                    .commit()
            },
            onEditarClick = { transportista ->
                // ─── COMPLETO: Acción Editar pasando el objeto seleccionado ───
                val fragmentoEditar = EditarTransportistaFragment()
                val bundle = Bundle()
                bundle.putSerializable("transportista", transportista)
                fragmentoEditar.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragmentoEditar)
                    .addToBackStack(null)
                    .commit()
            },
            onBajaClick = { transportista ->
                // Acción Baja: Llama a tu diálogo pasándole el transportista seleccionado
                mostrarDialogoEliminar(transportista)
            }
        )
        recyclerView.adapter = adapter

        // 3. Escuchador del buscador para filtrar en tiempo real
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 4. Botón flotante (+) -> Ir a Registrar
        view.findViewById<View>(R.id.fabAgregarTransportista)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarTransportistaFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    // 🔍 FUNCIÓN QUE EXTRAE LOS MOTORISTAS DE TU SQLITE
    private fun obtenerTransportistasDeBD(): List<Transportista> {
        val lista = mutableListOf<Transportista>()
        try {
            val db = dbHelper.openDatabase()
            val cursor: Cursor = db.rawQuery("SELECT * FROM TRANSPORTISTA", null)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_TRANSPORTISTA"))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_TRANSPORTISTA"))
                    val dui = cursor.getString(cursor.getColumnIndexOrThrow("DUI_TRANSPORTISTA"))
                    val nit = cursor.getString(cursor.getColumnIndexOrThrow("NIT_TRANSPORTISTA"))
                    val placa = cursor.getString(cursor.getColumnIndexOrThrow("PLACA_TRANSPORTISTA"))
                    val licencia = cursor.getString(cursor.getColumnIndexOrThrow("NO_LICENCIA"))
                    val tipoLicencia = cursor.getString(cursor.getColumnIndexOrThrow("TIPO_LICENCIA"))
                    val telefono = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_TRANSPORTISTA"))
                    val correo = cursor.getString(cursor.getColumnIndexOrThrow("CORREO_TRANSPORTISTA"))

                    lista.add(Transportista(id, nombre, dui, nit, placa, licencia, tipoLicencia, telefono, correo))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lista
    }

    // ─── DIÁLOGO CON LÓGICA DE BORRADO REAL ───
    private fun mostrarDialogoEliminar(transportista: Transportista) {
        val vistaDialogo = layoutInflater.inflate(R.layout.dialog_eliminar, null)

        val tvTitulo = vistaDialogo.findViewById<TextView>(R.id.tv_titulo_eliminar)
        val btnEliminar = vistaDialogo.findViewById<MaterialButton>(R.id.btn_confirmar_eliminar)
        val btnCancelar = vistaDialogo.findViewById<MaterialButton>(R.id.btn_cancelar_eliminar)

        tvTitulo?.text = "Confirmar Eliminación"
        btnEliminar?.text = "Si Eliminar"

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(vistaDialogo)
            .setCancelable(true)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnCancelar?.setOnClickListener { dialog.dismiss() }

        // AL DAR CLIC EN "SI ELIMINAR" BORRA DE LA BD LOCAL
        btnEliminar?.setOnClickListener {
            try {
                val db = dbHelper.openDatabase()
                // Borramos usando el ID único del transportista seleccionado
                val filasAfectadas = db.delete("TRANSPORTISTA", "ID_TRANSPORTISTA = ?", arrayOf(transportista.id.toString()))
                db.close()

                if (filasAfectadas > 0) {
                    Toast.makeText(context, "${transportista.nombre} dado de baja", Toast.LENGTH_SHORT).show()
                    // Recargamos la lista al instante
                    adapter.actualizarLista(obtenerTransportistasDeBD())
                } else {
                    Toast.makeText(context, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    // Recarga la lista automáticamente si regresas de insertar o editar un motorista nuevo
    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.actualizarLista(obtenerTransportistasDeBD())
        }
    }
}