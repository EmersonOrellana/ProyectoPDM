package com.example.proyectopdm

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonalFragment : Fragment(R.layout.fragment_personal) {

    private lateinit var dbHelper: DatabaseHelper
    private val listaEmpleados = ArrayList<UsuarioModel>()
    private lateinit var adapter: PersonalAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        val fabAgregar = view.findViewById<FloatingActionButton>(R.id.fabAgregar)
        val rvPersonal = view.findViewById<RecyclerView>(R.id.rvPersonal)

        rvPersonal.layoutManager = LinearLayoutManager(requireContext())

        // 1. Cargamos el personal activo de la BD
        cargarPersonalDesdeBD()

        // 2. Adaptador con el AlertDialog Sincronizado
        adapter = PersonalAdapter(listaEmpleados) { empleado, accion ->
            val bundle = Bundle()
            bundle.putString("correo_usuario", empleado.correo)

            when (accion) {
                "EDITAR" -> {
                    val frag = EditarPersonalFragment()
                    frag.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, frag)
                        .addToBackStack(null)
                        .commit()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Editar Personal"
                }
                "FICHA" -> {
                    val frag = FichaEmpleadoFragment()
                    frag.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, frag)
                        .addToBackStack(null)
                        .commit()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Ficha de Empleado"
                }
                "PROYECTO" -> {
                    val frag = ProyectosAsignadosFragment()
                    frag.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.content_container, frag)
                        .addToBackStack(null)
                        .commit()
                    activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Proyectos Asignados"
                }
                "BAJA" -> {
                    // 🔔 CALCADO DE TU DIALOG REAL
                    val dialogView = layoutInflater.inflate(R.layout.dialog_eliminar, null)

                    val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    builder.setView(dialogView)
                    val alertDialog = builder.create()

                    // Transparencia para respetar los bordes redondeados de tu CardView de 16dp
                    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    // 🆔 CORREGIDO: Mapeo exacto con guiones bajos (_) según tu XML
                    val btnConfirmar = dialogView.findViewById<MaterialButton>(R.id.btn_confirmar_eliminar)
                    val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btn_cancelar_eliminar)
                    val tvMensaje = dialogView.findViewById<TextView>(R.id.tv_mensaje_eliminar)

                    // Modificamos el texto dinámicamente
                    tvMensaje?.text = "¿Está seguro que desea dar de baja a ${empleado.nombre}?\nEsta acción no se puede deshacer."

                    // Acción Cancelar
                    btnCancelar?.setOnClickListener {
                        alertDialog.dismiss()
                    }

                    // Acción Confirmar (Baja lógica en SQLite)
                    btnConfirmar?.setOnClickListener {
                        alertDialog.dismiss()

                        val db = dbHelper.openDatabase()
                        try {
                            val valores = ContentValues().apply {
                                put("ESTADO", "Inactivo")
                            }

                            val filasAfectadas = db.update(
                                "USUARIO",
                                valores,
                                "CORREO_ELECTRONICO = ?",
                                arrayOf(empleado.correo)
                            )

                            if (filasAfectadas > 0) {
                                Toast.makeText(context, "¡${empleado.nombre} dado de baja!", Toast.LENGTH_SHORT).show()

                                // Refrescamos el RecyclerView
                                cargarPersonalDesdeBD()
                                adapter.notifyDataSetChanged()
                            } else {
                                Toast.makeText(context, "No se pudo actualizar en la base", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Error BD: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            db.close()
                        }
                    }

                    alertDialog.show()
                }
            }
        }

        rvPersonal.adapter = adapter

        // 3. Botón Flotante (+)
        fabAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.content_container, RegistrarPersonalFragment())
                .addToBackStack(null)
                .commit()

            activity?.findViewById<TextView>(R.id.tvHeaderTitle)?.text = "Registrar Personal"
        }
    }

    // 🔍 CONSULTA SQLITE SELECT
    private fun cargarPersonalDesdeBD() {
        listaEmpleados.clear()
        val db = dbHelper.openDatabase()
        var cursor: Cursor? = null

        try {
            val query = "SELECT * FROM USUARIO WHERE ESTADO = 'Activo' ORDER BY ID_USUARIO DESC"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_USUARIO"))
                    val idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL"))
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")) ?: ""
                    val apellido = cursor.getString(cursor.getColumnIndexOrThrow("APELLIDO_USUARIO")) ?: ""
                    val nit = cursor.getString(cursor.getColumnIndexOrThrow("NIT_USUARIO")) ?: ""
                    val correo = cursor.getString(cursor.getColumnIndexOrThrow("CORREO_ELECTRONICO")) ?: ""
                    val telefono = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_USUARIO")) ?: ""

                    listaEmpleados.add(UsuarioModel(id, idRol, nombre, apellido, nit, correo, telefono))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}