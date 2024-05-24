package programacion.kimberly.gym.ui.bar

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.app.Activity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import programacion.kimberly.gym.R

class UpdateDataActivity : AppCompatActivity() {
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextBirthYear: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    private lateinit var backButton: ImageButton
    private lateinit var buttonSelectPhoto: Button

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var storageReference: StorageReference

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextBirthYear = findViewById(R.id.editTextBirthYear)
        buttonSave = findViewById(R.id.buttonUpdateData)
        buttonCancel = findViewById(R.id.buttonCancel)
        backButton = findViewById(R.id.backButton)
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto)

        // Get reference to Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        if (FirebaseAuth.getInstance().currentUser != null) {
            loadUserData()
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }

        buttonSave.setOnClickListener {
            saveData()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        buttonCancel.setOnClickListener {
            loadUserData()
        }

        buttonSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun loadUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val userRef = firestore.collection("users").document(uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")
                        val phoneNumber = document.getLong("phoneNumber")
                        val birthYear = document.getLong("birthYear")

                        editTextFirstName.setText(firstName)
                        editTextLastName.setText(lastName)
                        editTextPhoneNumber.setText(phoneNumber?.toString())
                        editTextBirthYear.setText(birthYear?.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error retrieving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val phoneNumber = editTextPhoneNumber.text.toString().trim().toLongOrNull()
            val birthYear = editTextBirthYear.text.toString().trim().toIntOrNull()

            if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber == null || birthYear == null) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                return
            }

            val dataMap = hashMapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName,
                "phoneNumber" to phoneNumber,
                "birthYear" to birthYear
            )

            val userRef = firestore.collection("users").document(uid)
            userRef.update(dataMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error updating data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val currentUserUID = FirebaseAuth.getInstance().currentUser?.uid

            // Crea una referencia al archivo en el almacenamiento de Firebase
            val photoRef = storageReference.child("profile_photos").child("$currentUserUID.jpg")

            // Sube la foto al almacenamiento de Firebase
            val uploadTask = photoRef.putFile(selectedImageUri!!)

            // Escucha el resultado de la subida
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La foto se subió correctamente
                    // Obtén la URL de descarga del archivo
                    photoRef.downloadUrl.addOnSuccessListener { uri ->
                        // Crea un nuevo documento en la colección "profile_photos" con el currentUserUID como ID de documento
                        val userPhotoDocRef = firestore.collection("profile_photos").document(currentUserUID!!)

                        // Crea un mapa con los datos que deseas guardar
                        val photoProfileData = hashMapOf(
                            "userId" to currentUserUID,
                            "photoUrl" to uri.toString()
                        )

                        // Guarda el mapa de datos en el documento
                        userPhotoDocRef.set(photoProfileData)
                            .addOnSuccessListener {
                                // Documento creado exitosamente
                                Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                // Error al crear el documento
                                Toast.makeText(this, "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Error al subir la foto
                    Toast.makeText(this, "Error al subir la foto de perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}