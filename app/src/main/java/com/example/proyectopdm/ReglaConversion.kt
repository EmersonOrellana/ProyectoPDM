package com.example.proyectopdm

data class ReglaConversion(
    val idConversion: Int = 0,
    val idUnidadOrigen: Int,
    val idUnidadDestino: Int,
    val factorConversion: Double,
    val descripcionConversion: String
)