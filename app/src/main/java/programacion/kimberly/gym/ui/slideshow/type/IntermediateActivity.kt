package programacion.kimberly.gym.ui.slideshow.type

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import programacion.kimberly.gym.R
import programacion.kimberly.gym.ui.slideshow.intermediate.BarbellActivity
import programacion.kimberly.gym.ui.slideshow.intermediate.CableActivity
import programacion.kimberly.gym.ui.slideshow.intermediate.DumbbellActivity
import programacion.kimberly.gym.ui.slideshow.intermediate.KettlebellActivity

class IntermediateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intermediate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Volver atrás
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Configurar los CardViews
        findViewById<CardView>(R.id.barbellCardView).setOnClickListener {
            startActivity(Intent(this, BarbellActivity::class.java))
        }

        findViewById<CardView>(R.id.dumbbellCardView).setOnClickListener {
            startActivity(Intent(this, DumbbellActivity::class.java))
        }

        findViewById<CardView>(R.id.kettlebellCardView).setOnClickListener {
            startActivity(Intent(this, KettlebellActivity::class.java))
        }

        findViewById<CardView>(R.id.cableCardView).setOnClickListener {
            startActivity(Intent(this, CableActivity::class.java))
        }
    }
}