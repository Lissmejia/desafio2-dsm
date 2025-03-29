package edu.udb.desafio2

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class ListadoActivity : AppCompatActivity() {

    private lateinit var lvEstudiantes: ListView
    private lateinit var spinnerFiltroGrado: Spinner
    private lateinit var btnVolver: Button
    private lateinit var btnAgregarEstudiante: Button
    private var estudiantesFiltrados = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)

        lvEstudiantes = findViewById(R.id.lvEstudiantes)
        spinnerFiltroGrado = findViewById(R.id.spinnerFiltroGrado)
        btnVolver = findViewById(R.id.btnVolver)
        btnAgregarEstudiante = findViewById(R.id.btnAgregarEstudiante)

        val grados = MainActivity.estudiantesList.map { it.split(" - ")[1] }.distinct().toTypedArray()

        val adapterGrado = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltroGrado.adapter = adapterGrado


        spinnerFiltroGrado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                filtrarEstudiantes(grados[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnVolver.setOnClickListener {
            finish()
        }

        btnAgregarEstudiante.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // Navegar a la actividad de registro
            startActivity(intent)
        }

        lvEstudiantes.setOnItemClickListener { _, _, position, _ ->
            val estudianteSeleccionado = estudiantesFiltrados[position]
            val datos = estudianteSeleccionado.split(" - ")

            val intent = Intent(this, ActualizarActivity::class.java)
            intent.putExtra("nombre", datos[0].split(" ")[0])
            intent.putExtra("apellido", datos[0].split(" ")[1])
            intent.putExtra("grado", datos[1])
            intent.putExtra("materia", datos[2])
            intent.putExtra("nota", datos[3])
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val actualizado = data?.getBooleanExtra("actualizado", false) ?: false
            if (actualizado) {
                // Si se ha actualizado la lista, la volvemos a cargar
                val gradoSeleccionado = spinnerFiltroGrado.selectedItem.toString()
                filtrarEstudiantes(gradoSeleccionado)
            }
        }
    }

    private fun filtrarEstudiantes(grado: String) {
        estudiantesFiltrados.clear()
        estudiantesFiltrados.addAll(MainActivity.estudiantesList.filter { it.contains(grado) })

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, estudiantesFiltrados)
        lvEstudiantes.adapter = adapter
    }
}