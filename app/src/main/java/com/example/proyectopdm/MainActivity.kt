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
            // Cambiamos el título a "MI PERFIL" y abrimos el fragmento
            cambiarPantalla(VerPerfilFragment(), -1, "MI PERFIL")
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
        val botones = listOf(
            Triple(btnInicio, findViewById<ImageView>(R.id.iv_inicio), findViewById<TextView>(R.id.tv_inicio)),
            Triple(btnProveedores, findViewById<ImageView>(R.id.iv_proveedores), findViewById<TextView>(R.id.tv_proveedores)),
            Triple(btnMateriales, findViewById<ImageView>(R.id.iv_materiales), findViewById<TextView>(R.id.tv_materiales)),
            Triple(btnPersonal, findViewById<ImageView>(R.id.iv_personal), findViewById<TextView>(R.id.tv_personal)),
            Triple(btnAjustes, findViewById<ImageView>(R.id.iv_ajustes), findViewById<TextView>(R.id.tv_ajustes))
        )

        botones.forEach { (boton, icono, texto) ->
            val estaSeleccionado = (boton.id == idSeleccionado)

            if (estaSeleccionado) {
                // Fondo azul redondeado (usando un drawable o setBackgroundResource)
                boton.setBackgroundResource(R.drawable.bg_nav_selected)
                icono.setColorFilter(Color.WHITE)
                texto.setTextColor(Color.WHITE)
            } else {
                // Fondo blanco
                boton.setBackgroundColor(Color.TRANSPARENT)
                icono.setColorFilter(Color.parseColor("#757575"))
                texto.setTextColor(Color.parseColor("#757575"))
            }
        }
    }
}