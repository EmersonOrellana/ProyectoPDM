package com.example.proyectopdm

data class RolModel(
    val idRol: Int,
    val nombreRol: String,
    val codigoRol: Int,      // ¡Agregado para coincidir con tu BD!
    val descripcionRol: String
)