package com.example.proyectopdm

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RolesListaFragment : Fragment(R.layout.fragment_roles_lista) {

    private lateinit var dbHelper: DatabaseHelper
    private val listaRolesCompleta = ArrayList<RolModel>()
    private lateinit var adapter: RolesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Vincular componentes de tu interfaz real
        val fabNuevoRol = view.findViewById<FloatingActionButton>(R.id.fabNuevoRol)
        val rvRoles = view.findViewById<RecyclerView>(R.id.rvRoles)
        val etBuscar = view.findViewById<EditText>(R.id.etBuscar)

        rvRoles.layoutManager = LinearLayoutManager(requireContext())

        // 1. Cargar los roles de tu tabla real
        cargarRolesDesdeBD()

        // 2. Acoplar el adaptador con las acciones
        adapter = RolesAdapter(listaRolesCompleta) { rol, accion ->
            val bundle = Bundle()
            bundle.putInt("id_rol", rol.idRol)

            when (accion) {
                "EDITAR" -> {
                    val frag = EditarRolFragment()
                    frag.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, frag)
                        .commit()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
                }
                "ELIMINAR" -> {
                    mostrarDialogoEliminar(rol)
                }
            }
        }
        rvRoles.adapter = adapter

        // 3. Buscador funcional en tiempo real
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().lowercase().trim()
                val filtrados = listaRolesCompleta.filter {
                    it.nombreRol.lowercase().contains(texto) ||
                            it.descripcionRol.lowercase().contains(texto) ||
                            it.codigoRol.toString().contains(texto)
                }
                adapter.filtrarLista(filtrados)
            }
        })

        // 4. Botón Flotante para crear un nuevo Rol
        fabNuevoRol.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, NuevoRolFragment())
                .commit()
            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "COTMAN"
        }

        // 5. Control de retroceso nativo hacia Ajustes
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    regresarAlMenuAjustes()
                }
            }
        )
    }

    private fun regresarAlMenuAjustes() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.content_container, PerfilFragment())
            .commit()
        activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "AJUSTES"
    }

    // 🔍 SELECCIÓN FIEL A LAS COLUMNAS DE TU CAPTURA
    private fun cargarRolesDesdeBD() {
        listaRolesCompleta.clear()
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null
        try {
            // Consulta limpia apuntando a tu tabla ROL
            val query = "SELECT * FROM ROL ORDER BY ID_ROL ASC"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Mapeado idéntico a tus tipos de datos de la imagen
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL")) ?: ""
                    val codigo = cursor.getInt(cursor.getColumnIndexOrThrow("CODIGO_ROL"))
                    val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_ROL")) ?: ""

                    listaRolesCompleta.add(RolModel(id, nombre, codigo, descripcion))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al leer tabla ROL: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    // 🔔 VALIDACIÓN CON TU DIÁLOGO PERSONALIZADO
    private fun mostrarDialogoEliminar(rol: RolModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_eliminar, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnConfirmar = dialogView.findViewById<MaterialButton>(R.id.btn_confirmar_eliminar)
        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btn_cancelar_eliminar)
        val tvMensaje = dialogView.findViewById<TextView>(R.id.tv_mensaje_eliminar)

        tvMensaje?.text = "¿Está seguro que desea eliminar el rol '${rol.nombreRol}'?\nEsta acción afectará a los usuarios vinculados."

        btnCancelar?.setOnClickListener { alertDialog.dismiss() }

        btnConfirmar?.setOnClickListener {
            alertDialog.dismiss()
            val db = dbHelper.openDatabase()
            try {
                val filas = db.delete("ROL", "ID_ROL = ?", arrayOf(rol.idRol.toString()))
                if (filas > 0) {
                    Toast.makeText(context, "Rol eliminado de la base de datos", Toast.LENGTH_SHORT).show()
                    cargarRolesDesdeBD()
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: Revisa que no hayan usuarios usando este rol", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }
        alertDialog.show()
    }
}