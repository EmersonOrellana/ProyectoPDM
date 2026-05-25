package com.example.proyectopdm

import android.content.ContentValues
import android.content.Context

class ReglaConversionRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val TABLA = "CONVERSION_UNIDAD" // El nombre real en tu SQLite

    fun insertarRegla(regla: ReglaConversion): Long {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("ID_UNIDAD_ORIGEN", regla.idUnidadOrigen)
            put("ID_UNIDAD_DESTINO", regla.idUnidadDestino)
            put("FACTOR_CONVERSION", regla.factorConversion)
            put("DESCRIPCION_CONVERSION", regla.descripcionConversion)
        }
        val id = db.insert(TABLA, null, valores)
        db.close()
        return id
    }

    fun obtenerReglas(): List<ReglaConversion> {
        val lista = mutableListOf<ReglaConversion>()
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM $TABLA", null)

        if (cursor.moveToFirst()) {
            do {
                val regla = ReglaConversion(
                    idConversion = cursor.getInt(cursor.getColumnIndexOrThrow("ID_CONVERSION")),
                    idUnidadOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("ID_UNIDAD_ORIGEN")),
                    idUnidadDestino = cursor.getInt(cursor.getColumnIndexOrThrow("ID_UNIDAD_DESTINO")),
                    factorConversion = cursor.getDouble(cursor.getColumnIndexOrThrow("FACTOR_CONVERSION")),
                    descripcionConversion = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_CONVERSION"))
                )
                lista.add(regla)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarRegla(regla: ReglaConversion): Boolean {
        val db = dbHelper.openDatabase()
        val valores = ContentValues().apply {
            put("ID_UNIDAD_ORIGEN", regla.idUnidadOrigen)
            put("ID_UNIDAD_DESTINO", regla.idUnidadDestino)
            put("FACTOR_CONVERSION", regla.factorConversion)
            put("DESCRIPCION_CONVERSION", regla.descripcionConversion)
        }
        val filasAfectadas = db.update(TABLA, valores, "ID_CONVERSION = ?", arrayOf(regla.idConversion.toString()))
        db.close()
        return filasAfectadas > 0
    }

    fun eliminarRegla(id: Int): Boolean {
        val db = dbHelper.openDatabase()
        val filasAfectadas = db.delete(TABLA, "ID_CONVERSION = ?", arrayOf(id.toString()))
        db.close()
        return filasAfectadas > 0
    }
}