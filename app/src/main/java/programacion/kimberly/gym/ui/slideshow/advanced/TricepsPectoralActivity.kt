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

class TricepsPectoralActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_triceps_pectoral)
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

        val tricepsRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/triceps?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val pectoralRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/pectorals?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val dorsalRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/lats?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef") // Reemplaza con tu nueva API Key
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(tricepsRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TricepsPectoralActivity", "Error fetching triceps exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("TricepsPectoralActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val triceps = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(triceps)
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })

        client.newCall(pectoralRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TricepsPectoralActivity", "Error fetching pectoral exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("TricepsPectoralActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val pectorals = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(pectorals)
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                        }
                    }
                }
            }
        })

        client.newCall(dorsalRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TricepsPectoralActivity", "Error fetching dorsal exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("TricepsPectoralActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val dorsals = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(dorsals)
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