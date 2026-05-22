package com.example.proyectopdm

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var tvHeaderTitle: TextView
    private lateinit var incluirCabecera: View
    private lateinit var incluirNav: View
    private var ivUserIcon: ImageView? = null

    private lateinit var btnInicio: LinearLayout
    private lateinit var btnProveedores: LinearLayout
    private lateinit var btnMateriales: LinearLayout
    private lateinit var btnPersonal: LinearLayout
    private lateinit var btnAjustes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        incluirCabecera = findViewById(R.id.incluir_cabecera)
        incluirNav = findViewById(R.id.incluir_barra_navegacion)
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)

        // Inicializar icono y clic
        ivUserIcon = incluirCabecera.findViewById(R.id.iv_user_icon)
        ivUserIcon?.setImageResource(R.drawable.account_circle)

        ivUserIcon?.setOnClickListener {
            cambiarPantalla(PerfilFragment(), -1, "PERFIL")
        }

        // Inicializar botones navegación
        btnInicio = findViewById(R.id.nav_inicio)
        btnProveedores = findViewById(R.id.nav_proveedores)
        btnMateriales = findViewById(R.id.nav_materiales)
        btnPersonal = findViewById(R.id.nav_personal)
        btnAjustes = findViewById(R.id.nav_ajustes)

        if (savedInstanceState == null) {
            incluirCabecera.visibility = View.GONE
            incluirNav.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, LoginFragment())
                .commit()
        }

        btnInicio.setOnClickListener { cambiarPantalla(InicioFragment(), R.id.nav_inicio, "COTMAN") }
        btnProveedores.setOnClickListener { cambiarPantalla(ProveedoresFragment(), R.id.nav_proveedores, "PROVEEDORES") }
        btnMateriales.setOnClickListener { cambiarPantalla(MaterialesFragment(), R.id.nav_materiales, "MATERIALES") }
        btnPersonal.setOnClickListener { cambiarPantalla(PersonalFragment(), R.id.nav_personal, "PERSONAL") }
        btnAjustes.setOnClickListener { cambiarPantalla(AjustesFragment(), R.id.nav_ajustes, "AJUSTES") }
    }

    fun cambiarPantalla(fragmento: Fragment, idBoton: Int, titulo: String) {
        incluirCabecera.visibility = View.VISIBLE
        incluirNav.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragmento)
            .addToBackStack(null)
            .commit()

        tvHeaderTitle.text = titulo
        actualizarNavVisual(idBoton)
    }

    private fun actualizarNavVisual(idSeleccionado: Int) {
        val listaBotones = listOf(btnInicio, btnProveedores, btnMateriales, btnPersonal, btnAjustes)
        listaBotones.forEach { boton ->
            boton.setBackgroundColor(if (idSeleccionado != -1 && boton.id == idSeleccionado)
                Color.parseColor("#7A92A8") else Color.parseColor("#97B0C5"))
        }
    }
}