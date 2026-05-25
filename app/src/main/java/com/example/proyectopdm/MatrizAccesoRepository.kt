package com.example.proyectopdm

import android.content.ContentValues
import android.content.Context

class MatrizAccesoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // 1. Obtener todos los roles para el Spinner
    fun obtenerRoles(): List<RolModel> {
        val lista = mutableListOf<RolModel>()
        val db = dbHelper.openDatabase()
        val cursor = db.rawQuery("SELECT * FROM ROL", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(RolModel(
                    idRol = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ROL")),
                    nombreRol = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ROL")),
                    codigoRol = cursor.getInt(cursor.getColumnIndexOrThrow("CODIGO_ROL")),
                    descripcionRol = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_ROL"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // 2. Obtener la matriz cruzada para un Rol específico
    fun obtenerMatrizPorRol(idRol: Int): List<AccesoModulo> {
        val lista = mutableListOf<AccesoModulo>()
        val db = dbHelper.openDatabase()

        // ¡CORREGIDO! Usamos NOMBRE_OPCION como está en tu base de datos
        val query = """
            SELECT o.ID_OPCION, o.NOMBRE_OPCION, a.ID_ACCESO, a.PUEDE_VER, a.PUEDE_CREAR_EDITAR, a.PUEDE_ELIMINAR
            FROM OPCION_MENU o
            LEFT JOIN (
                SELECT ar.* FROM ACCESO_ROL ar
                INNER JOIN ES_ESTABLECIDO ee ON ar.ID_ACCESO = ee.ID_ACCESO
                WHERE ee.ID_ROL = ?
            ) a ON o.ID_OPCION = a.ID_OPCION
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idRol.toString()))

        if (cursor.moveToFirst()) {
            do {
                val idAccesoIndex = cursor.getColumnIndex("ID_ACCESO")
                val idAcceso = if (cursor.isNull(idAccesoIndex)) 0 else cursor.getInt(idAccesoIndex)

                val modulo = AccesoModulo(
                    idAcceso = idAcceso,
                    idOpcion = cursor.getInt(cursor.getColumnIndexOrThrow("ID_OPCION")),
                    // ¡CORREGIDO AQUÍ TAMBIÉN!
                    nombreModulo = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_OPCION")),
                    puedeVer = if (idAcceso != 0) cursor.getInt(cursor.getColumnIndexOrThrow("PUEDE_VER")) == 1 else false,
                    puedeEditar = if (idAcceso != 0) cursor.getInt(cursor.getColumnIndexOrThrow("PUEDE_CREAR_EDITAR")) == 1 else false,
                    puedeEliminar = if (idAcceso != 0) cursor.getInt(cursor.getColumnIndexOrThrow("PUEDE_ELIMINAR")) == 1 else false
                )
                lista.add(modulo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // 3. Guardar la configuración masiva
    fun guardarMatriz(idRol: Int, accesos: List<AccesoModulo>): Boolean {
        val db = dbHelper.openDatabase()
        db.beginTransaction()
        try {
            for (acceso in accesos) {
                val valores = ContentValues().apply {
                    put("ID_OPCION", acceso.idOpcion)
                    put("PUEDE_VER", if (acceso.puedeVer) 1 else 0)
                    put("PUEDE_CREAR_EDITAR", if (acceso.puedeEditar) 1 else 0)
                    put("PUEDE_ELIMINAR", if (acceso.puedeEliminar) 1 else 0)
                }

                if (acceso.idAcceso == 0) {
                    val nuevoIdAcceso = db.insert("ACCESO_ROL", null, valores)
                    val unionValores = ContentValues().apply {
                        put("ID_ROL", idRol)
                        put("ID_ACCESO", nuevoIdAcceso)
                    }
                    db.insert("ES_ESTABLECIDO", null, unionValores)
                } else {
                    db.update("ACCESO_ROL", valores, "ID_ACCESO = ?", arrayOf(acceso.idAcceso.toString()))
                }
            }
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}