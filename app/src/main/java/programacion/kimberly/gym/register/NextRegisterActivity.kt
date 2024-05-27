package programacion.kimberly.gym.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.R

class NextRegisterActivity : BaseActivity() {

    lateinit var editTextFirstName: EditText
    lateinit var editTextLastName: EditText
    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextBirthYear: EditText
    lateinit var editTextHeight: EditText
    lateinit var editTextWeight: EditText
    lateinit var radioGroupGender: RadioGroup
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var buttonContinue: Button

    var firestore = FirebaseFirestore.getInstance()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_register)

        // Inicialización de vistas
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextBirthYear = findViewById(R.id.editTextBirthYear)
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        buttonContinue = findViewById(R.id.buttonContinue)

        // Recuperar el UID del usuario desde el Intent
        val userUid = intent.getStringExtra("USER_UID")
        if (userUid.isNullOrEmpty()) {
            Toast.makeText(this, "Error: User ID is not available.", Toast.LENGTH_SHORT).show()
            finish()  // Finaliza la actividad si no hay UID
            return
        }

        buttonContinue.setOnClickListener {
            savePersonalInformation(userUid)
        }
    }

    fun savePersonalInformation(uid: String) {
        val selectedGenderId = radioGroupGender.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }

        if (gender.isEmpty()) {
            Toast.makeText(this, "Please select a gender.", Toast.LENGTH_SHORT).show()
            return
        }

        // Conviértelo a tipos numéricos apropiados antes de guardar
        val phoneNumber = editTextPhoneNumber.text.toString().trim().toLongOrNull() ?: 0L  // Convertir a Long
        val birthYear = editTextBirthYear.text.toString().trim().toIntOrNull() ?: 0  // Convertir a Int
        val height = editTextHeight.text.toString().trim().toDoubleOrNull() ?: 0.0  // Convertir a Double
        val weight = editTextWeight.text.toString().trim().toDoubleOrNull() ?: 0.0  // Convertir a Double

        val user = hashMapOf(
            "firstName" to editTextFirstName.text.toString().trim(),
            "lastName" to editTextLastName.text.toString().trim(),
            "phoneNumber" to phoneNumber,  // Almacenar como Long
            "birthYear" to birthYear,  // Almacenar como Int
            "height" to height,  // Almacenar como Double
            "weight" to weight,  // Almacenar como Double
            "gender" to gender  // Almacenar como String
        )

        firestore.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Personal information saved successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)  // Navegar a MainActivity
                startActivity(intent)
                finish()  // Finalizar esta actividad
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving personal information: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()  // Finalizar esta actividad en caso de error
            }
    }
}
