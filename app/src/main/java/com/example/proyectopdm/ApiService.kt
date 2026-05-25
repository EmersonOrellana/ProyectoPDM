package com.example.proyectopdm

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import okhttp3.ResponseBody

interface ApiService {
    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioRemoto>

    @POST("login") // Supongamos que tu ruta es 'login'
    suspend fun loginUsuario(@Body credenciales: LoginRequest): retrofit2.Response<LoginResponse>
    @POST("registrar")
    suspend fun registrarUsuario(@Body usuario: UsuarioRemoto): retrofit2.Response<ResponseBody>

}

object RetrofitClient {
    // Cambia la IP de 10.0.2.2 a tu IP real de Laragon
    private const val BASE_URL = "http://192.168.1.7/api_pdm/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}