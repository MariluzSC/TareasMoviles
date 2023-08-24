package com.example.practicapaises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val paisesListView: ListView = findViewById(R.id.paisesListView)
        val paises = arrayOf("Costa Rica", "Colombia", "Panamá")

        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)
        paisesListView.adapter = adaptador

        paisesListView.setOnItemClickListener { _, _, position, _ ->
            val paisSeleccionado = paises[position]
            val intent = Intent(this, InfoPaisesActivity::class.java)
            intent.putExtra("pais", paisSeleccionado)
            startActivity(intent)
        }
    }
}
