package com.example.vistas2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerExample()

    }

    fun suma(view:View){
        //traer los id de mis elementos
        val et1:EditText = findViewById(R.id.et1)
        val et2:EditText = findViewById(R.id.et2)
        val tvResultado:TextView = findViewById(R.id.tvResultado)

        var valor1 = et1.text.toString().toInt()
        var valor2 = et2.text.toString().toInt()

        /*val resultado = if (rSuma.isChecked)
            valor1 + valor2
        else
            valor1 - valor2

        tvResultado.text = (resultado).toString()*/
    }


    @SuppressLint("ResourceType")
    fun spinnerExample(){
        var lista = resources.getStringArray(R.array.Operaciones) //llamando la lista de operacioness
        val spinner: Spinner = findViewById(R.id.spinner)

        // Crea un ArrayAdapter usando los elementos y el diseño predeterminado para el spinner
        // val adapter = ArrayAdapter(this, R.array.Operaciones, android.R.layout.simple_spinner_item, lista)
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.Operaciones, android.R.layout.simple_spinner_item)

        // Especifica el diseño que se usará cuando se desplieguen las opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Une el ArrayAdapter al Spinner
        spinner.adapter = adapter

        // Definir los valores
        val et1:EditText = findViewById(R.id.et1)
        val et2:EditText = findViewById(R.id.et2)

        // Opcionalmente, puedes configurar un escuchador para detectar la selección del usuario
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOperation = parent?.getItemAtPosition(position).toString()
                performOperation(selectedOperation)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Se llama cuando no se ha seleccionado nada en el Spinner (opcional)
                Toast.makeText(this@MainActivity, "Nada", Toast.LENGTH_SHORT).show()
            }

            private fun performOperation(operation: String) {
                val et1: EditText = findViewById(R.id.et1)
                val et2: EditText = findViewById(R.id.et2)
                val tvResultado: TextView = findViewById(R.id.tvResultado)

                val text1 = et1.text.toString()
                val text2 = et2.text.toString()

                if (text1.isEmpty() || text2.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Ingrese ambos valores", Toast.LENGTH_SHORT).show()
                    return
                }

                val valor1 = text1.toInt()
                val valor2 = text2.toInt()

                var result = 0

                when (operation) {
                    "Sumar" -> result = valor1 + valor2
                    "Restar" -> result = valor1 - valor2
                    "Multiplicar" -> result = valor1 * valor2
                    "Dividir" -> {
                        if (valor2 != 0) {
                            result = valor1 / valor2
                        } else {
                            Toast.makeText(this@MainActivity, "No se puede dividir por cero", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                }
                tvResultado.text = "Resultado: $result"
            }
        }
    }
}
