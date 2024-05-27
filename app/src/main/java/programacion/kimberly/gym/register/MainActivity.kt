package programacion.kimberly.gym.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import programacion.kimberly.gym.BaseActivity
import programacion.kimberly.gym.FirstActivity
import programacion.kimberly.gym.R

class MainActivity : BaseActivity() {

    lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        //  Inicializar views
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val registerButton: Button = findViewById(R.id.buttonRegister)
        val forgotPasswordTextView: TextView = findViewById(R.id.textViewForgotPassword)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Inicio de sesión exitoso, actualiza la UI con la información del usuario
                            val intent = Intent(this, FirstActivity::class.java) // Cambiado a FirstActivity

                            Toast.makeText(baseContext, "Authenticated.",
                                Toast.LENGTH_SHORT).show()

                            startActivity(intent)
                            finish() // Finaliza la actividad actual para que el usuario no pueda volver a ella presionando el botón atrás
                        } else {
                            // Si el inicio de sesión falla, muestra un mensaje al usuario
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            // Navegar a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            // Navegar a la actividad para restablecer la contraseña
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        // Verificar si el usuario está logueado
        val currentUser = auth.currentUser
        if (currentUser != null) {

            // Usuario ya está logueado, ir a la pantalla principal
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}