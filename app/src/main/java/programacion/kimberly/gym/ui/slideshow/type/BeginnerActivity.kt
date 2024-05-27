package programacion.kimberly.gym.ui.slideshow.type

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.beginner.assisted.PilatesRoutineActivity
import programacion.kimberly.gym.ui.slideshow.beginner.assisted.StrengthCardioRoutineActivity
import programacion.kimberly.gym.ui.slideshow.beginner.assisted.StrengthRoutineActivity
import programacion.kimberly.gym.ui.slideshow.beginner.assisted.CardioRoutineActivity

class BeginnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beginner)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón de retroceso
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Configurar clics en las tarjetas
        findViewById<androidx.cardview.widget.CardView>(R.id.cardioRoutineCardView).setOnClickListener {
            startActivity(Intent(this, CardioRoutineActivity::class.java))
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.strengthCardioRoutineCardView).setOnClickListener {
            startActivity(Intent(this, StrengthCardioRoutineActivity::class.java))
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.strengthRoutineCardView).setOnClickListener {
            startActivity(Intent(this, StrengthRoutineActivity::class.java))
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.pilatesRoutineCardView).setOnClickListener {
            startActivity(Intent(this, PilatesRoutineActivity::class.java))
        }
    }
}
