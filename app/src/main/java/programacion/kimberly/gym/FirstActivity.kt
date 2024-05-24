package programacion.kimberly.gym

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.databinding.ActivityFirstBinding

class FirstActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityFirstBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarFirst.toolbar)

        binding.appBarFirst.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_first)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.first, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_first)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        updateNavHeader()
    }

    protected fun updateNavHeader() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val navView: NavigationView = binding.navView
            val headerView = navView.getHeaderView(0)
            val nameTextView: TextView = headerView.findViewById(R.id.textViewName)
            val emailTextView: TextView = headerView.findViewById(R.id.textViewEmail)
            val profileImageView: ImageView = headerView.findViewById(R.id.imageView)

            // Load the profile photo
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val profilePhotoRef = firestore.collection("profile_photos").document(userId)

                profilePhotoRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // If the document exists, get the profile photo URL and load it using Glide
                            val photoUrl = documentSnapshot.getString("photoUrl")
                            if (photoUrl != null) {
                                Glide.with(this)
                                    .load(photoUrl)
                                    .placeholder(R.drawable.default_profile_picture) // Placeholder image while loading the photo
                                    .error(R.drawable.default_profile_picture) // Default image in case of error
                                    .into(profileImageView)
                            } else {
                                // If the photo URL is null, load the default image from drawable
                                Glide.with(this)
                                    .load(R.drawable.default_profile_picture)
                                    .into(profileImageView)
                            }
                        } else {
                            // If the document does not exist, load the default image from drawable
                            Glide.with(this)
                                .load(R.drawable.default_profile_picture)
                                .into(profileImageView)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("FirstActivity", "get failed with ", exception)
                        // If there is an error, load the default image from drawable
                        Glide.with(this)
                            .load(R.drawable.default_profile_picture)
                            .into(profileImageView)
                    }
            }

            // Actualizar el correo electrónico
            emailTextView.text = user.email ?: "Correo no disponible"

            // Recuperar y actualizar el nombre del usuario desde Firestore
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        nameTextView.text = if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
                            "$firstName $lastName"
                        } else {
                            "Name not available"
                        }
                    } else {
                        Log.d("FirstActivity", "No such document")
                        nameTextView.text = "Name not available"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FirstActivity", "get failed with ", exception)
                    nameTextView.text = "Name not available"
                }
        }
    }
}