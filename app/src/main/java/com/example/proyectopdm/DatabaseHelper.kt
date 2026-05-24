package com.example.proyectopdm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "base_local.sqlite"
        private const val DATABASE_VERSION = 1
    }

    init {
        // Verifica si la base de datos ya existe en el teléfono
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase(dbFile) // Si no existe, la copia desde assets
        }
    }

    private fun copyDatabase(dbFile: File) {
        // Asegurarnos de que la carpeta interna de bases de datos exista
        dbFile.parentFile?.mkdirs()

        // Abre el archivo desde la carpeta assets
        context.assets.open(DATABASE_NAME).use { input ->
            // Crea el archivo de salida en el teléfono
            FileOutputStream(dbFile).use { output ->
                val buffer = ByteArray(1024)
                var length: Int
                // Copia bloque por bloque
                while (input.read(buffer).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }
                output.flush()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Queda vacío porque las tablas ya vienen creadas en el archivo
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Lógica para actualizaciones
    }

    // Método para acceder a los datos
    fun openDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(
            context.getDatabasePath(DATABASE_NAME).path,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }
}