package com.example.proyectopdm

import android.content.ContentValues
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
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase(dbFile)
        }
    }

    private fun copyDatabase(dbFile: File) {
        dbFile.parentFile?.mkdirs()
        context.assets.open(DATABASE_NAME).use { input ->
            FileOutputStream(dbFile).use { output ->
                val buffer = ByteArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }
                output.flush()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun openDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(
            context.getDatabasePath(DATABASE_NAME).path,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }

    fun validarUsuario(correo: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ? AND CONTRASENA = ?", arrayOf(correo, contrasena))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    fun registrarUsuario(nombres: String, apellidos: String, correo: String, contrasena: String): Boolean {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE_USUARIO", nombres)
            put("APELLIDO_USUARIO", apellidos)
            put("CORREO_ELECTRONICO", correo)
            put("CONTRASENA", contrasena)
            put("ESTADO", "Activo")
            put("DUI_USUARIO", "00000000-0")
            put("NIT_USUARIO", "0000-000000-000-0")
            put("FECHA_CONTRATACION", "2026-05-24")
            put("TELEFONO_USUARIO", "0000-0000")
            put("ID_ROL", 2)
        }
        val resultado = db.insert("USUARIO", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerCategorias(): List<Categoria> {
        val listaCategorias = ArrayList<Categoria>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM CATEGORIA", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_CATEGORIA"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_CATEGORIA"))
                val codigo = cursor.getString(cursor.getColumnIndexOrThrow("CODIGO_CATEGORIA"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION"))
                listaCategorias.add(Categoria(id, nombre, codigo, descripcion))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaCategorias
    }

    fun registrarTransportista(
        nombre: String, dui: String, nit: String, placa: String,
        licencia: String, tipoLicencia: String, telefono: String, correo: String
    ): Boolean {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE_TRANSPORTISTA", nombre)
            put("DUI_TRANSPORTISTA", dui)
            put("NIT_TRANSPORTISTA", nit)
            put("PLACA_TRANSPORTISTA", placa)
            put("NO_LICENCIA", licencia)
            put("TIPO_LICENCIA", tipoLicencia)
            put("TELEFONO_TRANSPORTISTA", telefono)
            put("CORREO_TRANSPORTISTA", correo)
        }
        val resultado = db.insert("TRANSPORTISTA", null, valores)
        db.close()
        return resultado != -1L
    }

    fun eliminarCategoria(idCategoria: Int): Boolean {
        val db = this.writableDatabase
        val resultado = db.delete("CATEGORIA", "ID_CATEGORIA=?", arrayOf(idCategoria.toString()))
        db.close()
        return resultado > 0
    }

    fun actualizarCategoria(id: Int, nombre: String, codigo: String, descripcion: String): Boolean {
        val db = this.openDatabase()
        val values = ContentValues().apply {
            put("NOMBRE_CATEGORIA", nombre)
            put("CODIGO_CATEGORIA", codigo)
            put("DESCRIPCION", descripcion)
        }
        val resultado = db.update("CATEGORIA", values, "ID_CATEGORIA = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun buscarCategorias(query: String): List<Categoria> {
        val lista = ArrayList<Categoria>()
        val db = this.openDatabase()
        val sql = "SELECT * FROM CATEGORIA WHERE NOMBRE_CATEGORIA LIKE ? OR CODIGO_CATEGORIA LIKE ?"
        val cursor = db.rawQuery(sql, arrayOf("%$query%", "%$query%"))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_CATEGORIA"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_CATEGORIA"))
                val codigo = cursor.getString(cursor.getColumnIndexOrThrow("CODIGO_CATEGORIA"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION"))
                lista.add(Categoria(id, nombre, codigo, descripcion))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}