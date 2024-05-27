package programacion.kimberly.gym.ui.slideshow.intermediate

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.ExerciseAdapter
import java.io.IOException

class BarbellActivity : AppCompatActivity() {

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
            .url("https://exercisedb.p.rapidapi.com/exercises/equipment/barbell?limit=10")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Activity", "Error fetching exercise", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("Activity", "Unexpected response code: ${response.code}")
                    if (response.code == 403) {
                        Log.e("Activity", "Possible reasons: Invalid API Key, exceeded usage limits, or incorrect API configuration.")
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