package programacion.kimberly.gym

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import programacion.kimberly.gym.ui.bar.UpdateDataActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.first, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_update_data -> {
                val intent = Intent(this, UpdateDataActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    protected fun loadProfileImage(profileImageView: ImageView, photoUrl: String?) {
        if (photoUrl != null) {
            Glide.with(profileImageView.context)
                .load(photoUrl)
                .placeholder(R.drawable.default_profile_picture)
                .error(R.drawable.default_profile_picture) // Default image en caso de error
                .into(profileImageView)
        } else {
            Glide.with(profileImageView.context)
                .load(R.drawable.default_profile_picture)
                .into(profileImageView)
        }
    }
}