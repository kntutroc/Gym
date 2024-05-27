package programacion.kimberly.gym.ui.slideshow.beginner.assisted

import android.os.Bundle
import android.util.Log
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

class StrengthRoutineActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_strength_routine)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)

        fetchEllipticalMachineExercises()
    }

    private fun fetchEllipticalMachineExercises() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/equipment/band?limit=10")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("StrengthCardioRoutineActivity", "Error fetching exercise", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("StrengthCardioRoutineActivity", "Unexpected response code: ${response.code}")
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