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
    //CRUD DE MATERIALES
    // Método para recuperar unidades reales de la BD
// Agrega esto a DatabaseHelper.kt
    fun recuperarUnidadesMedida(): List<Unidad> {
        val lista = ArrayList<Unidad>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT ID_UNIDAD, NOMBRE_UNIDAD FROM UNIDAD_MEDIDA", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Unidad(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun registrarMaterial(nombre: String, idCat: Int, idUni: Int, desc: String): Boolean {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE_MATERIAL", nombre)
            put("ID_CATEGORIA", idCat)
            put("ID_UNIDAD", idUni) // Ahora enviamos el entero
            put("DESCRIPCION", desc)
        }
        val resultado = db.insert("MATERIAL", null, valores)
        db.close()
        return resultado != -1L
    }
    fun obtenerMateriales(): List<Material> {
        val lista = ArrayList<Material>()
        val db = this.readableDatabase

        // Usamos JOIN para obtener los nombres reales en lugar de los IDs
        val sql = """
            SELECT M.ID_MATERIAL, M.NOMBRE_MATERIAL, C.NOMBRE_CATEGORIA, U.NOMBRE_UNIDAD, M.DESCRIPCION 
            FROM MATERIAL M
            INNER JOIN CATEGORIA C ON M.ID_CATEGORIA = C.ID_CATEGORIA
            INNER JOIN UNIDAD_MEDIDA U ON M.ID_UNIDAD = U.ID_UNIDAD
        """.trimIndent()

        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Material(
                    cursor.getInt(0),      // ID_MATERIAL
                    cursor.getString(1),   // NOMBRE_MATERIAL
                    cursor.getString(2),   // NOMBRE_CATEGORIA
                    cursor.getString(3),   // NOMBRE_UNIDAD
                    cursor.getString(4)    // DESCRIPCION
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    // Método para eliminar un material
    fun eliminarMaterial(idMaterial: Int): Boolean {
        val db = this.writableDatabase
        val resultado = db.delete("MATERIAL", "ID_MATERIAL = ?", arrayOf(idMaterial.toString()))
        db.close()
        return resultado > 0
    }

    // Método para buscar materiales por nombre
    fun buscarMateriales(query: String): List<Material> {
        val lista = ArrayList<Material>()
        val db = this.readableDatabase

        // Unimos con JOIN para mostrar los nombres reales al buscar
        val sql = """
            SELECT M.ID_MATERIAL, M.NOMBRE_MATERIAL, C.NOMBRE_CATEGORIA, U.NOMBRE_UNIDAD, M.DESCRIPCION 
            FROM MATERIAL M
            INNER JOIN CATEGORIA C ON M.ID_CATEGORIA = C.ID_CATEGORIA
            INNER JOIN UNIDAD_MEDIDA U ON M.ID_UNIDAD = U.ID_UNIDAD
            WHERE M.NOMBRE_MATERIAL LIKE ?
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf("%$query%"))
        if (cursor.moveToFirst()) {
            do {
                lista.add(Material(
                    cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarMaterial(id: Int, nombre: String, idCat: Int, idUni: Int, desc: String): Boolean {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE_MATERIAL", nombre)
            put("ID_CATEGORIA", idCat)
            put("ID_UNIDAD", idUni)
            put("DESCRIPCION", desc)
        }

        // El 'WHERE' es ID_MATERIAL = ?
        val resultado = db.update("MATERIAL", valores, "ID_MATERIAL = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }fun recargarDatosIniciales() {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // 1. Limpieza total de tablas
            db.execSQL("DELETE FROM MUNICIPIO")
            db.execSQL("DELETE FROM DEPARTAMENTO")

            // 2. Inserción de Departamentos
            val departamentos = mapOf(
                1 to "Ahuachapán", 2 to "Santa Ana", 3 to "Sonsonate", 4 to "Chalatenango",
                5 to "La Libertad", 6 to "San Salvador", 7 to "Cuscatlán", 8 to "La Paz",
                9 to "Cabañas", 10 to "San Vicente", 11 to "Usulután", 12 to "San Miguel",
                13 to "Morazán", 14 to "La Unión"
            )
            departamentos.forEach { (id, nombre) ->
                db.execSQL("INSERT INTO DEPARTAMENTO (ID_DEPARTAMENTO, NOMBRE_DEPARTAMENTO) VALUES ($id, '$nombre')")
            }

            // 3. Inserción de Municipios
            val municipios = mapOf(
                1 to listOf("Atiquizaya", "El Refugio", "San Lorenzo", "Turín", "Ahuachapán", "Apaneca", "Concepción de Ataco", "Tacuba", "Guaymango", "Jujutla", "San Francisco Menéndez", "San Pedro Puxtla"),
                2 to listOf("Masahuat", "Metapán", "Santa Rosa Guachipilín", "Texistepeque", "Santa Ana", "Candelaria de la Frontera", "Chalchuapa", "El Porvenir", "San Sebastián Salitrillo", "Santiago de la Frontera", "Coatepeque", "El Congo"),
                3 to listOf("Juayúa", "Nahuizalco", "Salcoatitán", "Santa Catarina Masahuat", "Sonsonate", "Sonzacate", "Izalco", "Nahulingo", "San Antonio del Monte", "Caluco", "Armenia", "Cuisnahuat", "Julian Alvarenga", "San Julián", "Santa Isabel Ishuatán", "Acajutla"),
                4 to listOf("Citalá", "La Palma", "San Ignacio", "Agua Caliente", "Dulce Nombre de María", "El Paraíso", "La Reina", "Nueva Concepción", "San Fernando", "San Francisco Morazán", "San Rafael", "Santa Rita", "Arcatao", "Azacualpa", "Cancasque", "Chalatenango", "Comalapa", "Concepción Quezaltepeque", "El Carrizal", "Las Vueltas", "Nombre de Jesús", "Nueva Trinidad", "Ojos de Agua", "Potonico", "San Antonio de la Cruz", "San Antonio Los Ranchos", "San Francisco Lempa", "San Isidro Labrador", "San José Las Flores", "San Luis del Carmen"),
                5 to listOf("Quezaltepeque", "San Matías", "San Pablo Tacachico", "San Juan Opico", "Ciudad Arce", "Colón", "Sacacoyo", "Tepecoyo", "Jayaque", "Talnique", "Antiguo Cuscatlán", "Huizúcar", "Nuevo Cuscatlán", "San José Villanueva", "Zaragoza", "Chiltiupán", "Jicalapa", "La Libertad", "Tamanique", "Teotepeque", "Santa Tecla", "Comasagua"),
                6 to listOf("Aguilares", "El Paisnal", "Guazapa", "Apopa", "Nejapa", "Ayutuxtepeque", "Mejicanos", "San Salvador", "San Marcos", "Santo Tomás", "Santiago Texacuangos", "Ilopango", "San Martín", "Soyapango", "Tonacatepeque", "Panchimalco", "Rosario de Mora"),
                7 to listOf("Suchitoto", "San José Guayabal", "Oratorio de Concepción", "San Bartolomé Perulapía", "San Pedro Perulapán", "Cojutepeque", "San Rafael Cedros", "Candelaria", "El Carmen", "El Rosario", "Monte San Juan", "San Cristóbal", "Santa Cruz Analquito", "Santa Cruz Michapa", "Tenancingo", "Ramón Grande", "San Ramón"),
                8 to listOf("San Pedro Masahuat", "Santiago Nonualco", "Santa María Ostuma", "El Rosario", "Jerusalén", "Mercedes La Ceiba", "Paraíso de Osorio", "San Antonio Masahuat", "San Emigdio", "San Juan Tepezontes", "San Miguel Tepezontes", "San Pedro Nonualco", "Tapalhuaca", "Cuyultitán", "Olocuilta", "San Francisco Chinameca", "San Juan Talpa", "San Luis Talpa", "San Luis La Herradura", "San Juan Nonualco", "San Rafael Obrajuelo", "Zacatecoluca"),
                9 to listOf("Sensuntepeque", "Victoria", "Dolores", "Guacotecti", "San Isidro", "Ilobasco", "Tejutepeque", "Jutiapa", "Cinquera"),
                10 to listOf("Apastepeque", "Santa Clara", "San Ildefonso", "San Esteban Catarina", "San Sebastián", "San Lorenzo", "San Vicente", "Guadalupe", "Verapaz", "Tepetitán", "Tecoluca", "San Cayetano Istepeque"),
                11 to listOf("Santiago de María", "Alegría", "Berlín", "Mercedes Umaña", "Jucuapa", "El Triunfo", "Nueva Granada", "San Bonaventura", "Usulután", "Jiquilisco", "Puerto El Triunfo", "San Dionisio", "Concepción Batres", "San Francisco Javier", "Santa Elena", "Santa María", "Tecapán", "Jucuarán", "San Agustín"),
                12 to listOf("Ciudad Barrios", "Sesori", "Nuevo Edén de San Juan", "San Gerardo", "San Luis de la Reina", "Carolina", "San Antonio", "Chapeltique", "San Miguel", "Comacarán", "Chirilagua", "Moncagua", "Quelepa", "Uluazapa", "Chinameca", "El Tránsito", "Lolotique", "Nueva Guadalupe", "San Jorge", "San Rafael Oriente"),
                13 to listOf("Arambala", "Cacaopera", "Corinto", "El Rosario", "Joateca", "Jocoaitique", "Meanguera", "Perquín", "San Fernando", "San Isidro", "Torola", "Chilanga", "Delicias de Concepción", "El Divisadero", "Gualococti", "Guatajiagua", "Jocoro", "Loloquique", "Osicala", "San Carlos", "San Francisco Gotera", "San Simón", "Sensembra", "Sociedad", "Yamabal", "Yoloaiquín"),
                14 to listOf("Anamorós", "Bolívar", "Concepción de Oriente", "El Sauce", "Lislique", "Nueva Esparta", "Pasaquina", "Polorós", "San José", "Conchagua", "El Carmen", "Intipucá", "La Unión", "Meanguera del Golfo", "San Alejo", "Yayantique", "Yucuaiquín")
            )

            var muniId = 1
            municipios.forEach { (deptoId, muniList) ->
                muniList.forEach { nombre ->
                    db.execSQL("INSERT INTO MUNICIPIO (ID_MUNICIPIO, ID_DEPARTAMENTO, NOMBRE_MUNICIPIO) VALUES ($muniId, $deptoId, '$nombre')")
                    muniId++
                }
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
        db.close()
    }
    // 1. Obtener todos los departamentos
    fun getAllDepartamentos(): List<Pair<Int, String>> {
        val lista = mutableListOf<Pair<Int, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT ID_DEPARTAMENTO, NOMBRE_DEPARTAMENTO FROM DEPARTAMENTO", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Pair(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    // 2. Obtener municipios por ID de departamento
//    fun getMunicipiosByDepto(idDepto: Int): List<String> {
//        val lista = mutableListOf<String>()
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT NOMBRE_MUNICIPIO FROM MUNICIPIO WHERE ID_DEPARTAMENTO = ?", arrayOf(idDepto.toString()))
//        if (cursor.moveToFirst()) {
//            do {
//                lista.add(cursor.getString(0))
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return lista
//    }

    // --- NUEVO MÉTODO: Insertar Proyecto ---
    fun insertProyecto(nombre: String, fecha: String, direccion: String, idMunicipio: Int): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE_PROYECTO", nombre)
            put("FECHA_INICIO", fecha)
            put("DIRECCION", direccion)
            put("ID_MUNICIPIO", idMunicipio)
            put("ESTADO", "Iniciado")

            // --- VALORES DE EMERGENCIA (Asegúrate de que el ID 1 exista en DB Browser) ---
            put("ID_USUARIO", 1)
            put("ID_DETALLEREQ", 1)
        }

        val id = db.insert("PROYECTO", null, valores)
        db.close()
        return id
    }
    // --- MODIFICACIÓN NECESARIA: Obtener Municipios con ID ---
    // Reemplaza tu método actual 'getMunicipiosByDepto' por este para poder guardar el ID
    fun getMunicipiosByDepto(idDepto: Int): List<Pair<Int, String>> {
        val lista = mutableListOf<Pair<Int, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT ID_MUNICIPIO, NOMBRE_MUNICIPIO FROM MUNICIPIO WHERE ID_DEPARTAMENTO = ?", arrayOf(idDepto.toString()))
        if (cursor.moveToFirst()) {
            do {
                // Guardamos ID y NOMBRE para poder usar el ID al guardar el proyecto
                lista.add(Pair(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun obtenerProyectos(): List<Proyecto> {
        val lista = ArrayList<Proyecto>()
        val db = this.readableDatabase
        // Seleccionamos específicamente lo que necesitamos
        val sql = "SELECT ID_PROYECTO, NOMBRE_PROYECTO, FECHA_INICIO, DIRECCION, ID_MUNICIPIO, ESTADO FROM PROYECTO"
        val cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {
                lista.add(Proyecto(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID_PROYECTO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_PROYECTO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FECHA_INICIO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DIRECCION")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID_MUNICIPIO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ESTADO"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun eliminarProyecto(id: Int): Boolean {
        val db = this.writableDatabase
        // Asegúrate de que el nombre de la columna sea ID_PROYECTO
        val resultado = db.delete("PROYECTO", "ID_PROYECTO = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun obtenerUsuarios(): List<Pair<Int, String>> {
        val lista = mutableListOf<Pair<Int, String>>()
        val db = this.readableDatabase
        // Ajusta los nombres de las columnas a como los tengas en tu BD
        val cursor = db.rawQuery("SELECT ID_USUARIO, NOMBRE_USUARIO, APELLIDO_USUARIO FROM USUARIO", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombreCompleto = "${cursor.getString(1)} ${cursor.getString(2)}"
                lista.add(Pair(id, nombreCompleto))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista

    }

    // Actualiza el campo ID_USUARIO en la tabla PROYECTO
    // Actualiza únicamente el encargado (ID_USUARIO)
    // 1. EL QUE ACTUALIZA (El que hace el trabajo sucio)
    fun actualizarProyectoEncargado(idProyecto: Int, idUsuario: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("ID_USUARIO", idUsuario)

        val resultado = db.update("PROYECTO", values, "ID_PROYECTO = ?", arrayOf(idProyecto.toString()))
        db.close()
        return resultado > 0
    }
    fun actualizarPassword(correo: String, nuevaPassword: String): Boolean {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("CONTRASENA", nuevaPassword) // Tu columna real
        }
        // Buscamos al usuario por su CORREO_ELECTRONICO
        val filasAfectadas = db.update("USUARIO", valores, "CORREO_ELECTRONICO = ?", arrayOf(correo))
        db.close()
        return filasAfectadas > 0 // Retorna true si encontró el correo y lo modificó
    }
    // Agrega este método a tu DatabaseHelper para obtener unidades vinculadas
    fun obtenerUnidadPorIdMaterial(idMaterial: Int): String {
        val db = this.readableDatabase
        var nombreUnidad = "U" // Valor por defecto
        val sql = "SELECT U.NOMBRE_UNIDAD FROM MATERIAL M INNER JOIN UNIDAD_MEDIDA U ON M.ID_UNIDAD = U.ID_UNIDAD WHERE M.ID_MATERIAL = ?"
        val cursor = db.rawQuery(sql, arrayOf(idMaterial.toString()))
        if (cursor.moveToFirst()) {
            nombreUnidad = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return nombreUnidad
    }

    fun obtenerIdUsuarioDelProyecto(idProyecto: Int): Int {
        val db = this.readableDatabase
        var id = 1 // Por defecto, si no hay encargado
        val cursor = db.rawQuery("SELECT ID_USUARIO FROM PROYECTO WHERE ID_PROYECTO = ?", arrayOf(idProyecto.toString()))
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return id
    }
    fun obtenerNombreEncargadoPorId(idUsuario: Int): String {
        val db = this.readableDatabase
        var nombre = "Sin asignar"
        val cursor = db.rawQuery("SELECT NOMBRE_USUARIO || ' ' || APELLIDO_USUARIO FROM USUARIO WHERE ID_USUARIO = ?", arrayOf(idUsuario.toString()))
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return nombre
    }

}
