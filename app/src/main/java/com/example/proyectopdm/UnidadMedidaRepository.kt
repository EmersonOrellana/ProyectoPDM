package com.example.proyectopdm

import android.content.ContentValues
import android.content.Context

class UnidadMedidaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Función para guardar
    fun insertarUnidad(unidad: UnidadMedida): Long {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("NOMBRE_UNIDAD", unidad.nombreUnidad)
            put("ABREVIATURA", unidad.abreviatura)
            put("DESCRIPCION_USO", unidad.descripcionUso)
        }
        val id = db.insert("UNIDAD_MEDIDA", null, valores)
        db.close()
        return id
    }

    // Función para leer la lista
    fun obtenerUnidades(): List<UnidadMedida> {
        val lista = mutableListOf<UnidadMedida>()
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM UNIDAD_MEDIDA", null)

        if (cursor.moveToFirst()) {
            do {
                val unidad = UnidadMedida(
                    idUnidad = cursor.getInt(cursor.getColumnIndexOrThrow("ID_UNIDAD")),
                    nombreUnidad = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_UNIDAD")),
                    abreviatura = cursor.getString(cursor.getColumnIndexOrThrow("ABREVIATURA")),
                    descripcionUso = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_USO"))
                )
                lista.add(unidad)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerUnidadPorId(id: Int): UnidadMedida? {
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM UNIDAD_MEDIDA WHERE ID_UNIDAD = ?", arrayOf(id.toString()))
        var unidad: UnidadMedida? = null
        if (cursor.moveToFirst()) {
            unidad = UnidadMedida(
                idUnidad = cursor.getInt(cursor.getColumnIndexOrThrow("ID_UNIDAD")),
                nombreUnidad = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_UNIDAD")),
                abreviatura = cursor.getString(cursor.getColumnIndexOrThrow("ABREVIATURA")),
                descripcionUso = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_USO"))
            )
        }
        cursor.close()
        db.close()
        return unidad
    }

    fun actualizarUnidad(unidad: UnidadMedida): Boolean {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("NOMBRE_UNIDAD", unidad.nombreUnidad)
            put("ABREVIATURA", unidad.abreviatura)
            put("DESCRIPCION_USO", unidad.descripcionUso)
        }
        val filasAfectadas = db.update("UNIDAD_MEDIDA", valores, "ID_UNIDAD = ?", arrayOf(unidad.idUnidad.toString()))
        db.close()
        return filasAfectadas > 0
    }

    // --- NUEVA FUNCIÓN PARA ELIMINAR ---
    fun eliminarUnidad(id: Int): Boolean {
        val db = dbHelper.openDatabase()
        // El método delete devuelve el número de filas eliminadas
        val filasAfectadas = db.delete("UNIDAD_MEDIDA", "ID_UNIDAD = ?", arrayOf(id.toString()))
        db.close()
        return filasAfectadas > 0
    }
}