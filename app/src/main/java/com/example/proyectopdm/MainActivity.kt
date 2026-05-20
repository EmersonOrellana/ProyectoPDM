package com.example.proyectopdm

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var tvHeaderTitle: TextView
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnProveedores: LinearLayout
    private lateinit var btnMateriales: LinearLayout
    private lateinit var btnPersonal: LinearLayout
    private lateinit var btnPerfil: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar el TextView del encabezado global compartido
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)

        // 2. Inicializar los botones contenedores de la barra de navegación inferior
        btnInicio = findViewById(R.id.nav_inicio)
        btnProveedores = findViewById(R.id.nav_proveedores)
        btnMateriales = findViewById(R.id.nav_materiales)
        btnPersonal = findViewById(R.id.nav_personal)
        btnPerfil = findViewById(R.id.nav_perfil) // El botón de la tuerca/perfil del mockup

        // 3. Pantalla de inicio por defecto al abrir la aplicación
        if (savedInstanceState == null) {
            cambiarPantalla(InicioProyectosFragment(), R.id.nav_inicio, "COTMAN")
        }

        // ================= CONTROLADORES DE CLIC PARA LA NAVEGACIÓN =================
        btnInicio.setOnClickListener {
            cambiarPantalla(InicioProyectosFragment(), R.id.nav_inicio, "COTMAN")
        }

        btnProveedores.setOnClickListener {
            cambiarPantalla(ProveedoresFragment(), R.id.nav_proveedores, "PROVEEDORES")
        }

        btnMateriales.setOnClickListener {
            cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES")
        }

        btnPersonal.setOnClickListener {
            cambiarPantalla(PersonalFragment(), R.id.nav_personal, "PERSONAL")
        }

        // CONEXIÓN FIEL: Al presionar este botón, carga el fragmento con el menú de Ajustes del Sistema
        btnPerfil.setOnClickListener {
            cambiarPantalla(AjustesFragment(), R.id.nav_perfil, "AJUSTES")
        }
    }

    /**
     * Reemplaza el fragmento actual en el contenedor central de la Activity principal
     */
    fun cambiarPantalla(fragmento: Fragment, idBoton: Int, titulo: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragmento)
            .commit()

        // Cambia el texto del encabezado superior dinámicamente
        tvHeaderTitle.text = titulo

        // Renderiza el cambio de color de fondo del menú inferior
        actualizarNavVisual(idBoton)
    }

    /**
     * Cambia los colores de los LinearLayout del menú para denotar cuál está activo
     */
    private fun actualizarNavVisual(idSeleccionado: Int) {
        val listaBotones = listOf(btnInicio, btnProveedores, btnMateriales, btnPersonal, btnPerfil)
        listaBotones.forEach { boton ->
            if (boton.id == idSeleccionado) {
                boton.setBackgroundColor(Color.parseColor("#816F6F")) // Color grisáceo de selección
            } else {
                boton.setBackgroundColor(Color.parseColor("#97B0C5")) // Color azul base del nav
            }
        }
    }
}