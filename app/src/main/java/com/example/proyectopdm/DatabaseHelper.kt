package com.example.proyectopdm

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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

    // Método para validar credenciales en la tabla USUARIO
    fun validarUsuario(correo: String, contrasena: String): Boolean {
        val db = this.readableDatabase

        // Consultamos si existe un registro que coincida con el correo y la contraseña
        val query = "SELECT * FROM USUARIO WHERE CORREO_ELECTRONICO = ? AND CONTRASENA = ?"
        val cursor = db.rawQuery(query, arrayOf(correo, contrasena))

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
            // Valores por defecto para cumplir con el 'not null' de la tabla
            put("ESTADO", "Activo")
            put("DUI_USUARIO", "00000000-0")
            put("NIT_USUARIO", "0000-000000-000-0")
            put("FECHA_CONTRATACION", "2026-05-24")
            put("TELEFONO_USUARIO", "0000-0000")
            put("ID_ROL", 2) // Asumiendo que 2 es Usuario Operativo
        }

        val resultado = db.insert("USUARIO", null, valores)
        db.close()
        return resultado != -1L
    }

    // Metodo para obtener las categorías desde la tabla CATEGORIA
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
        return listaCategorias
    }

    // ─── AGREGADO: MÉTODO PARA GUARDAR TRANSPORTISTAS NUEVOS DESDE EL FORMULARIO DE REGISTRO ───
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
}