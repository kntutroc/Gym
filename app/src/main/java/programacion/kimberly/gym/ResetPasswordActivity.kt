package programacion.kimberly.gym

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Inicializar la instancia de FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Referencias a las vistas
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
                            // Notificar al usuario que el correo electrónico ha sido enviado
                            Toast.makeText(this, "Reset password email sent.", Toast.LENGTH_SHORT).show()

                            // Navegar a MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Finaliza la actividad actual para que el usuario no pueda volver a ella presionando el botón Atrás
                        } else {
                            // Notificar al usuario que ha ocurrido un error
                            Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Notificar al usuario que el campo de correo electrónico está vacío
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}