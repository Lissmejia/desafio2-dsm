package edu.udb.desafio2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var spinnerGrado: Spinner
    private lateinit var spinnerMateria: Spinner
    private lateinit var etNotaFinal: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnVerEstudiantes: Button
    private lateinit var btnLogout: Button

    companion object {
        val estudiantesList = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        spinnerGrado = findViewById(R.id.spinnerGrado)
        spinnerMateria = findViewById(R.id.spinnerMateria)
        etNotaFinal = findViewById(R.id.etNotaFinal)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnVerEstudiantes = findViewById(R.id.btnVerEstudiantes)
        btnLogout = findViewById(R.id.btnLogout)

        // Datos para los Spinners
        val grados = arrayOf("Primer Grado", "Segundo Grado", "Tercer Grado")
        val materias = arrayOf("Matemáticas", "Ciencias", "Sociales")

        val adapterGrado = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrado.adapter = adapterGrado

        val adapterMateria = ArrayAdapter(this, android.R.layout.simple_spinner_item, materias)
        adapterMateria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateria.adapter = adapterMateria

        btnRegistrar.setOnClickListener {
            registrarNota()
            val intent = Intent(this, ListadoActivity::class.java)
            startActivity(intent)
        }

        btnVerEstudiantes.setOnClickListener {
            val intent = Intent(this, ListadoActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // Limpiar la pila de actividades
            startActivity(intent)
            finish()
        }

    }

    private fun registrarNota() {
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val grado = spinnerGrado.selectedItem.toString()
        val materia = spinnerMateria.selectedItem.toString()
        val nota = etNotaFinal.text.toString()

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || nota.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        val notaFinal = try {
            nota.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, getString(R.string.error_nota_invalida), Toast.LENGTH_SHORT).show()
            return
        }

        if (notaFinal < 0 || notaFinal > 10) {
            Toast.makeText(this, getString(R.string.error_nota_invalida), Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar en la lista global
        val estudiante = "$nombre $apellido - $grado - $materia - $notaFinal"
        estudiantesList.add(estudiante)

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

        etNombre.text.clear()
        etApellido.text.clear()
        etNotaFinal.text.clear()

        val intent = Intent(this, ListadoActivity::class.java)
        startActivity(intent)
    }
}

