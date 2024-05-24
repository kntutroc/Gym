package programacion.kimberly.gym.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.R

class MedidasActivity : BaseActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var editTextWaist: EditText
    private lateinit var editTextHip: EditText
    private lateinit var editTextBiceps: EditText
    private lateinit var editTextLeg: EditText
    private lateinit var buttonSaveMeasurements: Button
    private lateinit var buttonDeleteMeasurements: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medidas)

        editTextWaist = findViewById(R.id.editTextWaist)
        editTextHip = findViewById(R.id.editTextHip)
        editTextBiceps = findViewById(R.id.editTextBiceps)
        editTextLeg = findViewById(R.id.editTextLeg)
        buttonSaveMeasurements = findViewById(R.id.buttonSaveMeasurements)
        buttonDeleteMeasurements = findViewById(R.id.buttonDeleteMeasurements)
        backButton = findViewById<ImageButton>(R.id.backButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        backButton.setOnClickListener {
            onBackPressed()
        }

        buttonSaveMeasurements.setOnClickListener {
            guardarMedidas()
        }

        buttonDeleteMeasurements.setOnClickListener {
            borrarMedidas()
        }
    }

    private fun guardarMedidas() {
        val waist = editTextWaist.text.toString().toDoubleOrNull() ?: 0.0
        val hip = editTextHip.text.toString().toDoubleOrNull() ?: 0.0
        val biceps = editTextBiceps.text.toString().toDoubleOrNull() ?: 0.0
        val leg = editTextLeg.text.toString().toDoubleOrNull() ?: 0.0

        val medidas = hashMapOf(
            "waistMeasurement" to waist,
            "hipMeasurement" to hip,
            "bicepsMeasurement" to biceps,
            "legMeasurement" to leg
        )

        currentUser?.uid?.let { uid ->
            firestore.collection("measurements").document(uid)
                .set(medidas)
                .addOnSuccessListener {
                    // Éxito al guardar las medidas
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Medidas guardadas exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar esta actividad después de guardar las medidas
                }
                .addOnFailureListener { e ->
                    // Error al guardar las medidas
                    // Mostrar mensaje de error
                    Toast.makeText(this, "Error al guardar las medidas", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun borrarMedidas() {
        editTextWaist.text.clear()
        editTextHip.text.clear()
        editTextBiceps.text.clear()
        editTextLeg.text.clear()
    }
}
