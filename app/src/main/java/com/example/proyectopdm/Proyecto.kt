package com.example.proyectopdm

data class Proyecto(
    val id: Int = 0,
    val nombre: String,
    val fecha: String,
    val direccion: String,
    val idMunicipio: Int,
    val estado: String = "Iniciado" // Valor por defecto
)