package com.example.tarea_3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Locale

data class Evento(
    val nombre: String,
    val descripcion: String,
    val fecha: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : ArrayAdapter<*>
    private lateinit var listEvents: MutableList<String>
    private lateinit var listJSONEvents: MutableList<Evento>
    private lateinit var tipoAlmacen: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listEvents = mutableListOf()
        listJSONEvents = mutableListOf()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Agenda"

        // Obtener una referencia al objeto SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)
        // Recuperar valores de SharedPreferences
        tipoAlmacen = sharedPreferences.getString("tipoLectura", "").toString()
        val primeraVez: Boolean = sharedPreferences.getBoolean("primera", true)

        if (primeraVez){
            mostrarOpcionesGuardado()
            with(sharedPreferences.edit()) {
                putBoolean("primera", false)
                apply()
            }
        }
        else{
            if (tipoAlmacen.equals("SD")){
                Toast.makeText(this, "Cargando datos", Toast.LENGTH_SHORT).show()
                leerExterno()
            }
            else if (tipoAlmacen.equals("INTERN")) {
                Toast.makeText(this, "Cargando datos", Toast.LENGTH_SHORT).show()
                leerArchivoInterno()
            }
        }

        listViewExample()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Acción para el elemento de búsqueda

                mostrarDialog()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun listViewExample() {

        val listView: ListView = findViewById(R.id.eventos)

        // Crea un ArrayAdapter para mostrar los nombres en el ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listEvents)

        // Asocia el ArrayAdapter con el ListView
        listView.adapter = adapter

        // Configura un escuchador para el clic en los elementos del ListView
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nombreSeleccionado = listEvents[position]
                callActivity2(nombreSeleccionado)
            }
        }
    }

    fun mostrarDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.create_event, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        val calendarView: CalendarView = dialogView.findViewById(R.id.calendarView)
        val nameView: EditText = dialogView.findViewById(R.id.nameInput)
        val descripView: EditText = dialogView.findViewById(R.id.descriptionInput)
        var fechaSeleccionada : String = ""

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // Convierte la fecha seleccionada a un formato de cadena (por ejemplo, "dd/MM/yyyy")
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            fechaSeleccionada = dateFormat.format(selectedDate.time)
        }

        val btnSave: Button = dialogView.findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            if (tipoAlmacen.equals("SD")){
                // Crea un objeto Evento con la fecha seleccionada y otros datos
                val evento = Evento(nameView.text.toString(), descripView.text.toString(), fechaSeleccionada)
                // Guarda el objeto Evento en un archivo JSON interno
                escribirExterno(evento)
            }
            else if (tipoAlmacen.equals("INTERN")) {
                // Crea un objeto Evento con la fecha seleccionada y otros datos
                val evento = Evento(nameView.text.toString(), descripView.text.toString(), fechaSeleccionada)
                // Guarda el objeto Evento en un archivo JSON interno
                escribirArchivoInterno(evento)
            }

            dialog.dismiss() // Cierra el diálogo cuando se hace clic en el botón "Cerrar"
        }
        val btnCerrar: Button = dialogView.findViewById(R.id.btnClose)
        btnCerrar.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo cuando se hace clic en el botón "Cerrar"
        }

        dialog.show()
    }

    fun mostrarOpcionesGuardado() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.preferences, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        val btnSD: Button = dialogView.findViewById(R.id.btnExtern)
        btnSD.setOnClickListener {
            savePreferences("SD")
            dialog.dismiss() // Cierra el diálogo cuando se hace clic en el botón "Cerrar"
        }

        val btnInterno: Button = dialogView.findViewById(R.id.btnIntern)
        btnInterno.setOnClickListener {
            savePreferences("INTERN")
            dialog.dismiss() // Cierra el diálogo cuando se hace clic en el botón "Cerrar"
        }

        dialog.show()
    }

    fun savePreferences(opcion : String){
        // Obtener una referencia al objeto SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        // Editor para realizar cambios en SharedPreferences
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Almacenar un valor en SharedPreferences
        editor.putString("tipoLectura", opcion)
        editor.putBoolean("primera", true)
        editor.apply() // Guardar los cambios
    }

    fun escribirArchivoInterno(evento: Evento) {
        val filename = "archivo_eventos.json"

        listEvents.add(evento.nombre)
        adapter.notifyDataSetChanged()

        try {
            // Agrega el nuevo evento a la lista
            listJSONEvents.add(evento)

            val gson = Gson()
            val eventosJson = gson.toJson(listJSONEvents)

            // Escribir la cadena JSON en el archivo interno
            val outputStreamWriter = OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE))
            outputStreamWriter.write(eventosJson)
            outputStreamWriter.close()
            Toast.makeText(this@MainActivity, "Evento Guardado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun leerArchivoInterno() {
        val filename = "archivo_eventos.json"

        try {
            val fileInputStream = openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val contenido = StringBuilder()
            var linea: String? = bufferedReader.readLine()
            while (linea != null) {
                contenido.append(linea)
                linea = bufferedReader.readLine()
            }
            bufferedReader.close()

            // Ahora tienes la cadena JSON leída del archivo interno
            // Convierte la cadena JSON a un objeto Evento usando Gson
            val gson = Gson()
            val listaEventos = gson.fromJson<List<Evento>>(contenido.toString(), object : TypeToken<List<Evento>>() {}.type)

            for (evento in listaEventos) {
                listEvents.add(evento.nombre)
                listJSONEvents.add(evento)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para escribir un objeto Evento en un archivo externo
    fun escribirExterno( evento: Evento) {

        listEvents.add(evento.nombre)
        adapter.notifyDataSetChanged()
        // Agrega el nuevo evento a la lista
        listJSONEvents.add(evento)

        val gson = Gson()
        val eventosJson = gson.toJson(listJSONEvents)

        val file = File(getExternalFilesDir(null), "eventos.txt")

        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(eventosJson.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para leer un objeto Evento desde un archivo externo
    fun leerExterno() {
        val file = File(getExternalFilesDir(null), "eventos.json")
        try {
            val bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()

            // Convierte la cadena JSON a un objeto Evento usando Gson
            val gson = Gson()
            val listaEventos = gson.fromJson<List<Evento>>(stringBuilder.toString(), object : TypeToken<List<Evento>>() {}.type)

            for (evento in listaEventos) {
                listEvents.add(evento.nombre)
                listJSONEvents.add(evento)
            }
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    private fun buscarEventoPorNombre(nombre: String): Evento? {

        for (evento in listJSONEvents) {
            if (evento.nombre == nombre) {
                return evento
            }
        }
        return null // Si no se encuentra el evento, devuelve null
    }

    fun callActivity2(nombreSeleccionado: String) {
        // Crear un Intent para iniciar la Activity2
        val intent = Intent(this, MainActivity2::class.java)

        val eventoSeleccionado = buscarEventoPorNombre(nombreSeleccionado)

        if(eventoSeleccionado != null){
            // Agrega los datos del evento como extras en el Intent
            intent.putExtra("nombre", eventoSeleccionado.nombre)
            intent.putExtra("descripcion", eventoSeleccionado.descripcion)
            intent.putExtra("fecha", eventoSeleccionado.fecha)
        }
        else{
            // Agrega los datos del evento como extras en el Intent
            intent.putExtra("nombre", "a")
            intent.putExtra("descripcion", "b")
            intent.putExtra("fecha", "c")
        }

        // Iniciar la Activity2 utilizando el Intent
        startActivity(intent)
    }
}