package com.example.tarea_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    private lateinit var eventNameEditText: TextView
    private lateinit var eventDescripEditText: TextView
    private lateinit var eventDateEditText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        eventNameEditText = findViewById(R.id.eventName)
        eventDescripEditText = findViewById(R.id.eventDescrip)
        eventDateEditText = findViewById(R.id.eventDate)

        // Recuperar los datos del Intent
        val nombreEvento = intent.getStringExtra("nombre")
        val descripcionEvento = intent.getStringExtra("descripcion")
        val fechaEvento = intent.getStringExtra("fecha")

        // Asignar los datos a los EditText
        eventNameEditText.text = (nombreEvento)
        eventDescripEditText.text = (descripcionEvento)
        eventDateEditText.text = (fechaEvento)

        // Configurar el botón para volver a MainActivity2
        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}