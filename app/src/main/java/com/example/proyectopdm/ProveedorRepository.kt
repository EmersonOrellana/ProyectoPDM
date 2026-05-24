package com.example.proyectopdm

import android.content.ContentValues
import android.content.Context

class ProveedorRepository(context: Context) {

    // Obtenemos la conexión desde DatabaseHelper
    private val dbHelper = DatabaseHelper(context)

    fun obtenerProveedores(): List<Proveedor> {
        val lista = mutableListOf<Proveedor>()
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM PROVEEDOR", null)

        if (cursor.moveToFirst()) {
            do {
                val proveedor = Proveedor(
                    idProveedor = cursor.getInt(cursor.getColumnIndexOrThrow("ID_PROVEEDOR")),
                    nombreProveedor = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_PROVEEDOR")),
                    direccionProveedor = cursor.getString(cursor.getColumnIndexOrThrow("DIRECCION_PROVEEDOR")),
                    telefonoProveedor = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_PROVEEDOR")),
                    correoProveedor = cursor.getString(cursor.getColumnIndexOrThrow("CORREO_PROVEEDOR")),
                    notaProveedor = cursor.getString(cursor.getColumnIndexOrThrow("NOTA_PROVEEDOR"))
                )
                lista.add(proveedor)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // Función actualizada para recibir el objeto Proveedor completo
    fun insertarProveedor(proveedor: Proveedor): Long {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("NOMBRE_PROVEEDOR", proveedor.nombreProveedor)
            put("DIRECCION_PROVEEDOR", proveedor.direccionProveedor)
            put("TELEFONO_PROVEEDOR", proveedor.telefonoProveedor)
            put("CORREO_PROVEEDOR", proveedor.correoProveedor)
            put("NOTA_PROVEEDOR", proveedor.notaProveedor)
        }

        val id = db.insert("PROVEEDOR", null, valores)
        db.close()
        return id
    }

    // Obtener un solo proveedor por su ID para llenar el formulario
    fun obtenerProveedorPorId(id: Int): Proveedor? {
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM PROVEEDOR WHERE ID_PROVEEDOR = ?", arrayOf(id.toString()))
        var proveedor: Proveedor? = null

        if (cursor.moveToFirst()) {
            proveedor = Proveedor(
                idProveedor = cursor.getInt(cursor.getColumnIndexOrThrow("ID_PROVEEDOR")),
                nombreProveedor = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_PROVEEDOR")),
                direccionProveedor = cursor.getString(cursor.getColumnIndexOrThrow("DIRECCION_PROVEEDOR")),
                telefonoProveedor = cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_PROVEEDOR")),
                correoProveedor = cursor.getString(cursor.getColumnIndexOrThrow("CORREO_PROVEEDOR")),
                notaProveedor = cursor.getString(cursor.getColumnIndexOrThrow("NOTA_PROVEEDOR"))
            )
        }
        cursor.close()
        db.close()
        return proveedor
    }

    // Ejecutar el UPDATE en la base de datos
    fun actualizarProveedor(proveedor: Proveedor): Boolean {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("NOMBRE_PROVEEDOR", proveedor.nombreProveedor)
            put("DIRECCION_PROVEEDOR", proveedor.direccionProveedor)
            put("TELEFONO_PROVEEDOR", proveedor.telefonoProveedor)
            put("CORREO_PROVEEDOR", proveedor.correoProveedor)
            put("NOTA_PROVEEDOR", proveedor.notaProveedor)
        }
        val resultado = db.update("PROVEEDOR", valores, "ID_PROVEEDOR = ?", arrayOf(proveedor.idProveedor.toString()))
        db.close()
        return resultado > 0
    }

    // Ejecutar el DELETE en la base de datos
    fun eliminarProveedor(id: Int): Boolean {
        val db = dbHelper.openDatabase()
        // El método delete devuelve el número de filas afectadas
        val resultado = db.delete("PROVEEDOR", "ID_PROVEEDOR = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }
}