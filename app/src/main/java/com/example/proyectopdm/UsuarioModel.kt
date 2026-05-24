package com.example.proyectopdm

data class UsuarioModel(
    val idUsuario: Int,
    val idRol: Int,
    val nombre: String,
    val apellido: String,
    val nit: String,
    val correo: String,
    val telefono: String
)