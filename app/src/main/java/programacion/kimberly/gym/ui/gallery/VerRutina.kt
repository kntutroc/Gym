package programacion.kimberly.gym.ui.gallery

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.R
import programacion.kimberly.gym.databinding.ActivityVerRutinaBinding

class VerRutina : AppCompatActivity() {

    private lateinit var binding: ActivityVerRutinaBinding
    private val TAG = "VerRutinaActivity"
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressed()
        }

        val routineId = intent.getStringExtra("ROUTINE_ID")
        if (routineId != null) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                val routineRef = FirebaseFirestore.getInstance().collection("routines").document(uid).collection("userRoutines").document(routineId)

                routineRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val routineName = documentSnapshot.getString("name") ?: "Sin nombre"

                            // Obtener los datos del día de la semana y los ejercicios correspondientes
                            val routineData = documentSnapshot.get("days") as? Map<String, List<String>> ?: emptyMap()
                            val routineText = buildRoutineText(routineData)

                            // Mostrar los datos en la interfaz de usuario
                            binding.routineNameTextView.text = routineName
                            binding.routineDataTextView.text = routineText
                        } else {
                            Log.e(TAG, "No se encontró la rutina")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al obtener la rutina: ", e)
                    }
            } else {
                Log.e(TAG, "Error: Usuario no autenticado")
            }
        } else {
            Log.e(TAG, "Error: ID de rutina no proporcionado")
        }
    }

    private fun buildRoutineText(routineData: Map<String, List<String>>): SpannableStringBuilder {
        val sb = SpannableStringBuilder()
        for ((day, exercises) in routineData) {
            val dayStart = sb.length
            sb.append("$day:\n")
            sb.setSpan(StyleSpan(android.graphics.Typeface.BOLD), dayStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            for (exercise in exercises) {
                sb.append("- $exercise\n")
            }
            sb.append("\n") // Añadir un espacio entre los días
        }
        return sb
    }
}