package edu.udb.desafio2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class ActualizarActivity : AppCompatActivity() {

    private lateinit var etNombreActualizar: EditText
    private lateinit var etApellidoActualizar: EditText
    private lateinit var spinnerGradoActualizar: Spinner
    private lateinit var spinnerMateriaActualizar: Spinner
    private lateinit var etNotaActualizar: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnCancelar: Button

    private var estudiantePosicion: Int = -1
    private val grados = arrayOf("Primer Grado", "Segundo Grado", "Tercer Grado")
    private val materias = arrayOf("Matemáticas", "Ciencias", "Sociales")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)

        etNombreActualizar = findViewById(R.id.etNombreActualizar)
        etApellidoActualizar = findViewById(R.id.etApellidoActualizar)
        spinnerGradoActualizar = findViewById(R.id.spinnerGradoActualizar)
        spinnerMateriaActualizar = findViewById(R.id.spinnerMateriaActualizar)
        etNotaActualizar = findViewById(R.id.etNotaActualizar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val adapterGrado = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGradoActualizar.adapter = adapterGrado

        val adapterMateria = ArrayAdapter(this, android.R.layout.simple_spinner_item, materias)
        adapterMateria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateriaActualizar.adapter = adapterMateria

        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""
        val grado = intent.getStringExtra("grado") ?: ""
        val materia = intent.getStringExtra("materia") ?: ""
        val nota = intent.getStringExtra("nota") ?: ""

        if (nombre.isEmpty() || apellido.isEmpty() || grado.isEmpty() || materia.isEmpty() || nota.isEmpty()) {
            Toast.makeText(this, "Faltan datos del estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        etNombreActualizar.setText(nombre)
        etApellidoActualizar.setText(apellido)
        etNotaActualizar.setText(nota)

        spinnerGradoActualizar.setSelection(grados.indexOf(grado))
        spinnerMateriaActualizar.setSelection(materias.indexOf(materia))

        estudiantePosicion = MainActivity.estudiantesList.indexOf("$nombre $apellido - $grado - $materia - $nota")

        btnActualizar.setOnClickListener {
            actualizarEstudiante()
        }

        btnEliminar.setOnClickListener {
            eliminarEstudiante()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun actualizarEstudiante() {
        val nombre = etNombreActualizar.text.toString().trim()
        val apellido = etApellidoActualizar.text.toString().trim()
        val grado = spinnerGradoActualizar.selectedItem.toString()
        val materia = spinnerMateriaActualizar.selectedItem.toString()
        val nota = etNotaActualizar.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || nota.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben ser llenados", Toast.LENGTH_SHORT).show()
            return
        }

        val notaFinal = try {
            nota.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "La nota debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (notaFinal < 0 || notaFinal > 10) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear nuevo registro
        val estudianteActualizado = "$nombre $apellido - $grado - $materia - $notaFinal"

        // Actualizar el estudiante en la lista
        if (estudiantePosicion >= 0) {
            MainActivity.estudiantesList[estudiantePosicion] = estudianteActualizado
            Toast.makeText(this, "Estudiante actualizado con éxito", Toast.LENGTH_SHORT).show()

            // Enviar resultado a ListadoEstudiantesActivity
            val intent = Intent()
            intent.putExtra("actualizado", true)
            setResult(RESULT_OK, intent)

            finish() // Cierra la actividad
        } else {
            Toast.makeText(this, "No se encontró el estudiante", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarEstudiante() {
        if (estudiantePosicion >= 0) {
            MainActivity.estudiantesList.removeAt(estudiantePosicion)

            val intent = Intent()
            intent.putExtra("actualizado", true)
            setResult(RESULT_OK, intent)

            Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "No se encontró el estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}
