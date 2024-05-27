package programacion.kimberly.gym.ui.gallery

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import programacion.kimberly.gym.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class BuscarEjerciciosActivity : AppCompatActivity() {

    private lateinit var exercisesTextView: TextView
    private lateinit var muscleSearchEditText: EditText
    private lateinit var difficultySpinner: Spinner
    private var isSpinnerInitialSelection = true // Bandera para controlar la selección inicial del Spinner
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_ejercicios)


        // Inicializar los views
        exercisesTextView = findViewById(R.id.exercisesTextView)
        muscleSearchEditText = findViewById(R.id.muscleSearchEditText)
        difficultySpinner = findViewById(R.id.difficultySpinner)
        backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressed()
        }


        // Configurar el adaptador del Spinner con las opciones de dificultad
        val difficulties = arrayOf("All", "Beginner", "Intermediate", "Expert")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultySpinner.adapter = spinnerAdapter

        // Escuchar los cambios en el texto de búsqueda (para cambios en vivo)
        muscleSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                triggerSearch()
            }
        })

        // Escuchar los cambios en la selección del Spinner
        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (!isSpinnerInitialSelection) {
                    triggerSearch()
                } else {
                    isSpinnerInitialSelection = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun triggerSearch() {
        val muscle = muscleSearchEditText.text.toString().trim()
        val difficulty = difficultySpinner.selectedItem.toString().toLowerCase()
        searchExercisesByMuscleAndDifficulty(muscle, difficulty)
    }

    private fun searchExercisesByMuscleAndDifficulty(muscle: String, difficulty: String) {
        val apiBaseUrl = "https://api.api-ninjas.com/v1/exercises"
        val apiKey = "t/BcF3GrFaiBlAowPbuH8w==I4TONEUtBQHtGTqt"

        lifecycleScope.launch(Dispatchers.IO) {
            // Configuramos la API
            val urlBuilder = StringBuilder(apiBaseUrl)
            if (muscle.isNotEmpty()) {
                urlBuilder.append("?muscle=").append(URLEncoder.encode(muscle, "UTF-8"))
            }
            if (difficulty.isNotEmpty() && difficulty != "all") {
                if (muscle.isNotEmpty()) {
                    urlBuilder.append("&")
                } else {
                    urlBuilder.append("?")
                }
                urlBuilder.append("difficulty=").append(URLEncoder.encode(difficulty, "UTF-8"))
            }

            val urlString = urlBuilder.toString()
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

            // Actualizar el TextView con los datos
            withContext(Dispatchers.Main) {
                if (response.isNullOrEmpty()) {
                    exercisesTextView.text = "No exercises found for this search"
                } else {
                    try {
                        val exercises = parseExercises(response)
                        if (exercises.isEmpty()) {
                            exercisesTextView.text = "No exercises found for this search"
                        } else {
                            val spannableStringBuilder = SpannableStringBuilder()
                            exercises.forEach { exercise ->
                                val nameSpannable = SpannableString("${exercise.name}\n")
                                nameSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, exercise.name.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                spannableStringBuilder.append(nameSpannable)

                                val details = "Type: ${exercise.type}\nMuscle: ${exercise.muscle}\nEquipment: ${exercise.equipment}\n"
                                spannableStringBuilder.append(details)

                                val difficultySpannable = SpannableString("${exercise.difficulty}\n\n")
                                val difficultyColor = when (exercise.difficulty.toLowerCase()) {
                                    "beginner" -> ContextCompat.getColor(this@BuscarEjerciciosActivity, R.color.beginner_color)
                                    "intermediate" -> ContextCompat.getColor(this@BuscarEjerciciosActivity, R.color.intermediate_color)
                                    "expert" -> ContextCompat.getColor(this@BuscarEjerciciosActivity, R.color.advanced_color)
                                    else -> Color.TRANSPARENT
                                }
                                difficultySpannable.setSpan(BackgroundColorSpan(difficultyColor), 0, exercise.difficulty.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                spannableStringBuilder.append(difficultySpannable)
                            }
                            exercisesTextView.text = spannableStringBuilder
                        }
                    } catch (e: Exception) {
                        exercisesTextView.text = "Error occurred during the search"
                    }
                }
            }
        }
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