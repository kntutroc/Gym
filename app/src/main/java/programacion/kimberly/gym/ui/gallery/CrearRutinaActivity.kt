package programacion.kimberly.gym.ui.gallery

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import programacion.kimberly.gym.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class CrearRutinaActivity : AppCompatActivity() {

    private lateinit var muscleAutoCompleteTextView: EditText
    private lateinit var exerciseSpinner: Spinner
    private lateinit var searchExerciseButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var spinnerContainer: LinearLayout
    private var exercisesList: List<Exercise> = emptyList()
    private lateinit var routineNameEditText: EditText
    private lateinit var daySpinner: Spinner
    private lateinit var saveDayButton: Button
    private lateinit var saveRoutineButton: Button

    // Para almacenar los ejercicios de cada día
    private val routineData = hashMapOf<String, MutableList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        // Inicializar los views
        muscleAutoCompleteTextView = findViewById(R.id.muscleAutoCompleteTextView)
        exerciseSpinner = findViewById(R.id.exerciseSpinner)
        searchExerciseButton = findViewById(R.id.searchExerciseButton)
        backButton = findViewById(R.id.backButton)
        spinnerContainer = findViewById(R.id.spinnerContainer)
        routineNameEditText = findViewById(R.id.routineNameEditText)
        daySpinner = findViewById(R.id.daySpinner)
        saveDayButton = findViewById(R.id.saveDayButton)
        saveRoutineButton = findViewById(R.id.saveRoutineButton)


        backButton.setOnClickListener {
            onBackPressed()
        }

        searchExerciseButton.setOnClickListener {
            val muscle = muscleAutoCompleteTextView.text.toString()
            if (muscle.isNotEmpty()) {
                searchExercisesByMuscle(muscle)
            }
        }

        saveDayButton.setOnClickListener {
                saveDay()
        }

        saveRoutineButton.setOnClickListener {
                saveRoutine()
        }


        val addExerciseButton: Button = findViewById(R.id.addExerciseButton)
        addExerciseButton.setOnClickListener {
            addNewExerciseSpinner()
        }

        val removeExerciseButton: Button = findViewById(R.id.removeExerciseButton)
        removeExerciseButton.setOnClickListener {
            removeLastExerciseSpinner()
        }
    }

    private fun saveDay() {
        val selectedDay = daySpinner.selectedItem.toString()
        val selectedExercises = getSelectedExercises()

        // Si el día ya existe en el mapa, obtiene la lista existente, de lo contrario crea una nueva
        val exercisesForDay = routineData.getOrPut(selectedDay) { mutableListOf() }

        // Agrega todos los ejercicios seleccionados a la lista del día
        exercisesForDay.clear() // Limpia la lista actual para evitar duplicados
        exercisesForDay.addAll(selectedExercises)

        Toast.makeText(this, "Day saved successfully.", Toast.LENGTH_SHORT).show()
        clearExerciseSpinners() // Limpia los Spinners para el siguiente día
    }

    private fun clearExerciseSpinners() {
        spinnerContainer.removeAllViews()
    }

    private fun saveRoutine() {
        val routineName = routineNameEditText.text.toString().trim()

        if (routineName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for the routine.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Unauthenticated user.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el nombre de la rutina ya existe
        val routinesCollection = FirebaseFirestore.getInstance()
            .collection("routines")
            .document(currentUser.uid)
            .collection("userRoutines")

        routinesCollection.whereEqualTo("name", routineName).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Guardar la rutina si el nombre no existe
                    val fullRoutineData = hashMapOf(
                        "name" to routineName,
                        "days" to routineData
                    )
                    routinesCollection.add(fullRoutineData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Routine saved successfully.", Toast.LENGTH_SHORT).show()
                            finish() // Regresar a la pantalla anterior
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving routine: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "A routine with that name already exists.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error checking routine name: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getSelectedExercises(): List<String> {
        val exercises = mutableListOf<String>()
        for (i in 0 until spinnerContainer.childCount) {
            val spinner = spinnerContainer.getChildAt(i) as? Spinner
            spinner?.selectedItem?.toString()?.let {
                exercises.add(it)
            }
        }
        return exercises
    }

    private fun searchExercisesByMuscle(muscle: String) {
        val apiBaseUrl = "https://api.api-ninjas.com/v1/exercises"
        val apiKey = "t/BcF3GrFaiBlAowPbuH8w==I4TONEUtBQHtGTqt"

        lifecycleScope.launch(Dispatchers.IO) {
            val urlString = "$apiBaseUrl?muscle=${URLEncoder.encode(muscle, "UTF-8")}"
            val response = try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("accept", "application/json")
                connection.setRequestProperty("X-Api-Key", apiKey)
                BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                if (response.isNullOrEmpty()) {
                    Log.e("Exercises", "Response is null or empty")
                } else {
                    try {
                        val exercises = parseExercises(response)
                        updateExerciseSpinner(exercises)
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    private fun updateExerciseSpinner(exercises: List<Exercise>) {
        exercisesList = exercises // Guarda la lista de ejercicios
        val exerciseNames = exercises.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseSpinner.adapter = adapter
    }

    private fun removeLastExerciseSpinner() {
        // Verifica si hay Spinners
        if (spinnerContainer.childCount > 0) {
            // Elimina el último Spinner agregado
            spinnerContainer.removeViewAt(spinnerContainer.childCount - 1)
        }
    }

    private fun addNewExerciseSpinner() {
        val newSpinner = Spinner(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8) // Ajusta los márgenes si es necesario
        newSpinner.layoutParams = layoutParams

        // Configura el adapter con la lista de ejercicios
        val exerciseNames = exercisesList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newSpinner.adapter = adapter

        // Añade el nuevo Spinner al contenedor
        spinnerContainer.addView(newSpinner)
    }


    private fun parseExercises(response: String): List<Exercise> {
        val mapper = ObjectMapper()
        val rootNode = mapper.readTree(response)
        return rootNode.map {
            Exercise(
                name = it.get("name").asText(),
                type = it.get("type").asText(),
                muscle = it.get("muscle").asText(),
                equipment = it.get("equipment").asText(),
                difficulty = it.get("difficulty").asText(),
                instructions = it.get("instructions").asText(),
            )
        }
    }
}