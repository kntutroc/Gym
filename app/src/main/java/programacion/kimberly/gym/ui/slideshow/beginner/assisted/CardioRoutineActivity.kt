package programacion.kimberly.gym.ui.slideshow.beginner.assisted

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import okhttp3.*
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.ExerciseAdapter
import java.io.IOException

class CardioRoutineActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_routine)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        viewPager = findViewById(R.id.viewPager)

        fetchCardioExercises()
    }

    private fun fetchCardioExercises() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/bodyPart/cardio?limit=10")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CardioRoutineActivity", "Error fetching exercise", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("CardioRoutineActivity", "Unexpected response code: ${response.code}")
                    if (response.code == 403) {
                        Log.e("CardioRoutineActivity", "Possible reasons: Invalid API Key, exceeded usage limits, or incorrect API configuration.")
                    }
                    return
                }

                response.body?.let { responseBody ->
                    val exercises = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    runOnUiThread {
                        exerciseAdapter = ExerciseAdapter(exercises)
                        viewPager.adapter = exerciseAdapter
                    }
                }
            }
        })
    }
}