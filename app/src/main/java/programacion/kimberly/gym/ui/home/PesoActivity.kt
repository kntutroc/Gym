package programacion.kimberly.gym.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.R

class PesoActivity : BaseActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_peso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val buttonDeleteMeasurements = findViewById<Button>(R.id.buttonDeleteMeasurements)
        buttonDeleteMeasurements.setOnClickListener {
            clearMeasurements()
        }

        val buttonSaveMeasurements = findViewById<Button>(R.id.buttonSaveMeasurements)
        buttonSaveMeasurements.setOnClickListener {
            saveMeasurements()
        }
    }

    private fun clearMeasurements() {
        // Limpiar los campos de texto
        findViewById<EditText>(R.id.editTextHeight).setText("")
        findViewById<EditText>(R.id.editTextWeight).setText("")

        // Mostrar mensaje de éxito
        Toast.makeText(this, "Medidas borradas", Toast.LENGTH_SHORT).show()
    }

    private fun saveMeasurements() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let { userId ->
            val heightText = findViewById<EditText>(R.id.editTextHeight).text.toString()
            val weightText = findViewById<EditText>(R.id.editTextWeight).text.toString()

            if (heightText.isBlank() || weightText.isBlank()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return
            }

            val height = heightText.toDouble()
            val weight = weightText.toDouble()

            // Guardar los datos en Firestore
            firestore.collection("users").document(userId)
                .update(
                    mapOf(
                        "height" to height,
                        "weight" to weight
                    )
                )
                .addOnSuccessListener {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Medidas actualizadas", Toast.LENGTH_SHORT).show()

                    // Regresar a la actividad anterior
                    onBackPressed()
                }
                .addOnFailureListener { e ->
                    // Mostrar mensaje de error
                    Toast.makeText(this, "Error al guardar las medidas: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
