package com.example.proyectopdm

data class Transportista(
    val id: Int,
    val nombre: String,
    val dui: String,
    val nit: String,
    val placa: String,
    val licencia: String,
    val tipoLicencia: String,
    val telefono: String,
    val correo: String
) : java.io.Serializable