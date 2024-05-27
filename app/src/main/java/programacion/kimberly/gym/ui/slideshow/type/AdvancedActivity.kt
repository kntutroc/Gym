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
import programacion.kimberly.gym.ui.slideshow.advanced.GlutesQuadsCalvesActivity
import programacion.kimberly.gym.ui.slideshow.advanced.TricepsPectoralActivity
import programacion.kimberly.gym.ui.slideshow.advanced.AbductorsAdductorsActivity
import programacion.kimberly.gym.ui.slideshow.advanced.BicepsUpperBackAbsActivity

class AdvancedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advanced)
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

        findViewById<CardView>(R.id.abductorsAdductorsCardView).setOnClickListener {
            startActivity(Intent(this, AbductorsAdductorsActivity::class.java))
        }

        findViewById<CardView>(R.id.tricepsPectoralCardView).setOnClickListener {
            startActivity(Intent(this, TricepsPectoralActivity::class.java))
        }

        findViewById<CardView>(R.id.glutesQuadsCalvesCardView).setOnClickListener {
            startActivity(Intent(this, GlutesQuadsCalvesActivity::class.java))
        }

        findViewById<CardView>(R.id.bicepsUpperBackAbsCardView).setOnClickListener {
            startActivity(Intent(this, BicepsUpperBackAbsActivity::class.java))
        }
    }
}