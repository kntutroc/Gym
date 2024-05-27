package programacion.kimberly.gym.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.R

class RegisterActivity : BaseActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar auth
        auth = FirebaseAuth.getInstance()

        // Bindeamos los views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos el usuario con la autenticación del usuario
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Updateamos la UI con los datos creados
                    val user = auth.currentUser
                    val uid = user?.uid
                    Toast.makeText(baseContext, "Authentication successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, NextRegisterActivity::class.java).apply {
                        // Pasamos el UID al otro activity
                        putExtra("USER_UID", uid)
                    }
                    startActivity(intent)
                    finish() // Finish this activity
                } else {
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}