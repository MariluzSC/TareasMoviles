package com.example.practicapaises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class InfoPaisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_paises)

        val infoSpinner: Spinner = findViewById(R.id.infoSpinner)
        val infoTextView: TextView = findViewById(R.id.infoTextView)
        val botonAceptar: Button = findViewById(R.id.botonAceptar)
        val botonCancelar: Button = findViewById(R.id.botonCancelar)

        val pais = intent.getStringExtra("pais")

        infoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> infoTextView.text = pais?.let { obtenerDescripcionPais(it) }
                    1 -> infoTextView.text = pais?.let { obtenerContinentePais(it) }
                    2 -> infoTextView.text = pais?.let { obtenerPoblacionPais(it) }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        botonAceptar.setOnClickListener {
            val mensajeToast = "El usuario vio ${infoSpinner.selectedItem} de $pais"
            Toast.makeText(this, mensajeToast, Toast.LENGTH_SHORT).show()
            finish()
        }

        botonCancelar.setOnClickListener { finish() }
    }

    private fun obtenerDescripcionPais(pais: String): String {
        return when (pais) {
            "Costa Rica" -> "Es un país lleno de biodiversidad"
            "Colombia" -> "Es un país con cultura y tradiciones muy interesantes"
            "Panamá" -> "Es un país que ha invertido en mejorar sus infraestructuras"
            else -> ""
        }
    }

    private fun obtenerContinentePais(pais: String): String {
        return when (pais) {
            "Costa Rica", "Panamá" -> "América Central"
            "Colombia" -> "América del Sur"
            else -> ""
        }
    }

    private fun obtenerPoblacionPais(pais: String): String {
        return when (pais) {
            "Costa Rica" -> "5,154"
            "Colombia" -> "51,52 millones"
            "Panamá" -> "4,351"
            else -> ""
        }
    }
}
