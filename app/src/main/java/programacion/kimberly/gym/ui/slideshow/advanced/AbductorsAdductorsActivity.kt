package programacion.kimberly.gym.ui.slideshow.advanced

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

class AbductorsAdductorsActivity : AppCompatActivity() {

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

        fetchExercises()
    }

    private fun fetchExercises() {
        val client = OkHttpClient()
        val exercises = mutableListOf<ExerciseAdapter.Exercise>()

        val abductorsRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/abductors?limit=3")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val adductorsRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/adductors?limit=3")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(abductorsRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AbductorsAdductorsActivity", "Error fetching abductors exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("AbductorsAdductorsActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val abductors = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(abductors)
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })

        client.newCall(adductorsRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AbductorsAdductorsActivity", "Error fetching adductors exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("AbductorsAdductorsActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val adductors = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(adductors)
                    if (exercises.size == 4) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })
    }
}