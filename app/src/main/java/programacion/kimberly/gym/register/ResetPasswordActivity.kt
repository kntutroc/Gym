package programacion.kimberly.gym.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.R

class ResetPasswordActivity : BaseActivity() {

    lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Inicializar firebase auth
        auth = FirebaseAuth.getInstance()

        // Inicializar los views
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val buttonResetPassword: Button = findViewById(R.id.buttonResetPassword)

        // Establecer el OnClickListener para el botón
        buttonResetPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                // Enviar correo electrónico de restablecimiento de contraseña
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset password email sent.", Toast.LENGTH_SHORT).show()

                            // Navegar a MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Finaliza la actividad actual para que el usuario no pueda volver a ella presionando el botón atrás
                        } else {
                            Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}