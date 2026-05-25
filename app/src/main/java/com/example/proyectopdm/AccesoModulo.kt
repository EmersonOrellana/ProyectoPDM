package com.example.proyectopdm

data class AccesoModulo(
    var idAcceso: Int = 0, // 0 significa que este rol aún no tiene configurado este módulo
    val idOpcion: Int,
    val nombreModulo: String,
    var puedeVer: Boolean = false,
    var puedeEditar: Boolean = false,
    var puedeEliminar: Boolean = false
)