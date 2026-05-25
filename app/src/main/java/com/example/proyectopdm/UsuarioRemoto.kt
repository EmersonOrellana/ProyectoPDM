package com.example.proyectopdm

import com.google.gson.annotations.SerializedName

data class UsuarioRemoto(
    // El PHP no parece estar esperando ID_USUARIO ni ID_ROL en el POST,
    // pero mantenemos la estructura de tu clase.
    @SerializedName("id_usuario") val ID_USUARIO: Int,
    @SerializedName("id_rol") val ID_ROL: Int,

    @SerializedName("nombre_usuario")
    val NOMBRE_USUARIO: String,

    @SerializedName("apellido_usuario")
    val APELLIDO_USUARIO: String,

    @SerializedName("correo_electronico")
    val CORREO_ELECTRONICO: String,

    @SerializedName("contrasena")
    val CONTRASENA: String


)

data class LoginRequest(
    @SerializedName("correo_electronico") val correo_electronico: String,
    @SerializedName("contrasena") val contrasena: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("usuario") val usuario: UsuarioRemoto?
)