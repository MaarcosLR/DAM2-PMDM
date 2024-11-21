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

    private lateinit var tb : Toolbar
    private lateinit var cv1 : CardView
    private lateinit var cv2 : CardView
    private lateinit var btnGo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primera_pantalla)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        btnGo.setOnClickListener{ navigateToSegundaPantalla() }
    }

    private fun initComponents() {

        tb = findViewById(R.id.tb)
        cv1 = findViewById(R.id.cv1)
        cv2 = findViewById(R.id.cv2)
        btnGo = findViewById(R.id.btnGo)

    }

    private fun navigateToSegundaPantalla() {

        val intent = Intent(this, SegundaPantalla::class.java)
        startActivity(intent)

    }
}