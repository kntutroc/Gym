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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.ExerciseAdapter
import java.io.IOException

class BicepsUpperBackAbsActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_biceps_upper_back_abs)
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
        Log.d("BicepsUpperBackAbsActivity", "ViewPager initialized")

        fetchExercises()
    }

    private fun fetchExercises() {
        val client = OkHttpClient()
        val exercises = mutableListOf<ExerciseAdapter.Exercise>()

        val bicepsRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/biceps?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val upperBackRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/upper%20back?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        val absRequest = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/abs?limit=2")
            .get()
            .addHeader("X-RapidAPI-Key", "ceaf127508msh509756a80e73741p1c1babjsn06366a5de1ef")
            .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(bicepsRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("BicepsUpperBackAbsActivity", "Error fetching biceps exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("BicepsUpperBackAbsActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val biceps = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(biceps)
                    Log.d("BicepsUpperBackAbsActivity", "Biceps exercises size: ${exercises.size}")
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                            Log.d("BicepsUpperBackAbsActivity", "Adapter set with exercises")
                        }
                    }
                }
            }
        })

        client.newCall(upperBackRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("BicepsUpperBackAbsActivity", "Error fetching upper back exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("BicepsUpperBackAbsActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val upperBack = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(upperBack)
                    Log.d("BicepsUpperBackAbsActivity", "Upper back exercises size: ${exercises.size}")
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                            Log.d("BicepsUpperBackAbsActivity", "Adapter set with exercises")
                        }
                    }
                }
            }
        })

        client.newCall(absRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("BicepsUpperBackAbsActivity", "Error fetching abs exercises", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("BicepsUpperBackAbsActivity", "Unexpected response code: ${response.code}")
                    return
                }

                response.body?.let { responseBody ->
                    val abs = Gson().fromJson(responseBody.string(), Array<ExerciseAdapter.Exercise>::class.java).toList()
                    exercises.addAll(abs)
                    Log.d("BicepsUpperBackAbsActivity", "Abs exercises size: ${exercises.size}")
                    if (exercises.size == 6) {
                        runOnUiThread {
                            exerciseAdapter = ExerciseAdapter(exercises)
                            viewPager.adapter = exerciseAdapter
                            Log.d("BicepsUpperBackAbsActivity", "Adapter set with exercises")
                        }
                    }
                }
            }
        })
    }
}