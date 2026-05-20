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

        // SOLUCIÓN AL ERROR: Buscamos el TextView directamente.
        // Si da error, asegúrate que layout_header.xml tenga el id: tvHeaderTitle
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)

        // Inicializar botones del menú (asegúrate que layout_bottom_nav tenga estos IDs)
        btnInicio = findViewById(R.id.nav_inicio)
        btnProveedores = findViewById(R.id.nav_proveedores)
        btnMateriales = findViewById(R.id.nav_materiales)
        btnPersonal = findViewById(R.id.nav_personal)
        btnPerfil = findViewById(R.id.nav_perfil)

        if (savedInstanceState == null) {
            cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN")
        }

        btnInicio.setOnClickListener { cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN") }
        btnProveedores.setOnClickListener { cambiarPantalla(ProveedoresFragment(), R.id.nav_proveedores, "PROVEEDORES") }
        btnMateriales.setOnClickListener { cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES") }
        btnPersonal.setOnClickListener { cambiarPantalla(PersonalFragment(), R.id.nav_personal, "PERSONAL") }
        btnPerfil.setOnClickListener { cambiarPantalla(AjustesFragment(), R.id.nav_perfil, "PERFIL") }
    }

    fun cambiarPantalla(fragmento: Fragment, idBoton: Int, titulo: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragmento)
            .commit()

        tvHeaderTitle.text = titulo
        actualizarNavVisual(idBoton)
    }

    private fun actualizarNavVisual(idSeleccionado: Int) {
        val listaBotones = listOf(btnInicio, btnProveedores, btnMateriales, btnPersonal, btnPerfil)
        listaBotones.forEach { boton ->
            if (boton.id == idSeleccionado) {
                // UN GRIS OPACO SELECCIONADO (Estilo Slate/Gris Azulado)
                boton.setBackgroundColor(Color.parseColor("#7A92A8"))
            } else {
                // EL COLOR ORIGINAL DE TU BARRA
                boton.setBackgroundColor(Color.parseColor("#97B0C5"))
            }
        }
    }
}