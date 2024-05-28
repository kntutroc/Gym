package programacion.kimberly.gym.ui.home

import android.content.Intent
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
import programacion.kimberly.gym.register.MainActivity

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

        checkUserStatus()
    }

    private fun checkUserStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Si no hay usuario autenticado, redirigir al MainActivity
            redirectToMainActivity()
            return
        }

        // Suscripción a cambios en el documento del usuario
        firestore.collection("users").document(user.uid)
            .addSnapshotListener { userDocument, error ->
                if (error != null) {
                    // Manejar el error
                    return@addSnapshotListener
                }

                if (userDocument != null) {
                    // Verificar si el usuario está deshabilitado
                    val isDisabled = userDocument.getBoolean("disabled") ?: false
                    if (isDisabled) {
                        // Si el usuario está deshabilitado, cerrar sesión y redirigir al MainActivity
                        FirebaseAuth.getInstance().signOut()
                        redirectToMainActivity()
                    }
                }
            }
    }

    private fun redirectToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
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
                    Toast.makeText(this, "Measurements saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar esta actividad después de guardar las medidas
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving measurements", Toast.LENGTH_SHORT).show()
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
