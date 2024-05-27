package programacion.kimberly.gym.ui.slideshow.advanced

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import okhttp3.*
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.ExerciseAdapter
import java.io.IOException

class GlutesQuadsCalvesActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_glutes_quads_calves)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        val glutesRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/glutes?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val quadsRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/quads?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val calvesRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/calves?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(glutesRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GlutesQuadsCalvesActivity", "Error fetching glutes exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("GlutesQuadsCalvesActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val glutes = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(glutes)
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })

        client.newCall(quadsRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GlutesQuadsCalvesActivity", "Error fetching quads exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("GlutesQuadsCalvesActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val quads = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(quads)
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })

        client.newCall(calvesRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GlutesQuadsCalvesActivity", "Error fetching calves exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("GlutesQuadsCalvesActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val calves = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(calves)
                    if (exercises.size == 6) {
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