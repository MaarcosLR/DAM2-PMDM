package com.example.fightrecsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrimeraPantalla : AppCompatActivity() {

    // Declaración de las variables para los componentes de la interfaz de usuario
    private lateinit var tb : Toolbar  // Barra de herramientas (Toolbar)
    private lateinit var cv1 : CardView  // Primer CardView
    private lateinit var cv2 : CardView  // Segundo CardView
    private lateinit var btnGo : Button  // Botón para navegar a la segunda pantalla

    // Método que se ejecuta cuando la actividad se crea
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Activa el modo "Edge-to-Edge" para aprovechar toda la pantalla
        setContentView(R.layout.activity_primera_pantalla)  // Establece el layout para esta actividad

        // Configura el comportamiento de las barras de sistema (barra de estado, barra de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())  // Obtiene los márgenes de las barras del sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)  // Aplica los márgenes a la vista
            insets  // Retorna los insets para continuar con el comportamiento predeterminado
        }

        // Inicializa los componentes de la interfaz de usuario
        initComponents()

        // Configura el comportamiento del botón para navegar a la segunda pantalla
        btnGo.setOnClickListener{ navigateToSegundaPantalla() }
    }

    // Método para inicializar los componentes de la interfaz de usuario
    private fun initComponents() {
        tb = findViewById(R.id.tb)  // Inicializa la Toolbar
        cv1 = findViewById(R.id.cv1)  // Inicializa el primer CardView
        cv2 = findViewById(R.id.cv2)  // Inicializa el segundo CardView
        btnGo = findViewById(R.id.btnGo)  // Inicializa el botón de navegación
    }

    // Método para navegar hacia la SegundaPantalla cuando se hace clic en el botón
    private fun navigateToSegundaPantalla() {
        val intent = Intent(this, SegundaPantalla::class.java)  // Crea un Intent para navegar a SegundaPantalla
        startActivity(intent)  // Inicia la SegundaPantalla
    }
}
