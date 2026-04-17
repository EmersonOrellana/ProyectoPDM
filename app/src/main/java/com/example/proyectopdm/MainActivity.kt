package com.example.proyectopdm

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Cargar el fragmento de Inicio por defecto al abrir la app
        if (savedInstanceState == null) {
            cambiarFragmento(InicioFragment())
        }

        // 2. Configurar los clics de tu BottomNav (usando los IDs que pusimos)
        val btnInicio = findViewById<LinearLayout>(R.id.nav_inicio)
        val btnPersonal = findViewById<LinearLayout>(R.id.nav_personal)
        val btnPerfil = findViewById<LinearLayout>(R.id.nav_perfil)

        btnInicio.setOnClickListener { cambiarFragmento(InicioFragment()) }

        btnPersonal.setOnClickListener {
            // crea el PersonalFragment y cámbialo aquí
            // cambiarFragmento(PersonalFragment())
        }

        btnPerfil.setOnClickListener {
            // cambiarFragmento(PerfilFragment())
        }
    }

    // 3. Función maestra para intercambiar fragmentos
    private fun cambiarFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragment) // Reemplaza lo que hay en el FrameLayout
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // Añade un efecto suave
            .commit()
    }
}