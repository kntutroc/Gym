package programacion.kimberly.gym.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import programacion.kimberly.gym.MainActivity
import programacion.kimberly.gym.R
import programacion.kimberly.gym.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var userDocumentListener: ListenerRegistration
    private lateinit var measurementsDocumentListener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Redirect to MainActivity if there is no authenticated user
            Toast.makeText(context, "No user signed in.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            activity?.finish()
            return
        }

        // Set up click listeners for buttons leading to MedidasActivity
        binding.expandOptionsButton.setOnClickListener {
            startActivity(Intent(activity, PesoActivity::class.java))
        }

        binding.updateMeasurementsButton.setOnClickListener {
            startActivity(Intent(activity, MedidasActivity::class.java))
        }

        // Subscribe to changes in the user document
        userDocumentListener = firestore.collection("users").document(user.uid)
            .addSnapshotListener { userDocument, error ->
                if (error != null) {
                    // Handle the error
                    Toast.makeText(context, "Error retrieving user data: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (userDocument != null && userDocument.exists()) {
                    // Get user data
                    val firstName = userDocument.getString("firstName") ?: ""
                    val lastName = userDocument.getString("lastName") ?: ""
                    val fullName = "$firstName $lastName"
                    val gender = userDocument.getString("gender") ?: "Gender not available"
                    val height = userDocument.getDouble("height") ?: 0.0
                    val weight = userDocument.getDouble("weight") ?: 0.0

                    // Update the UI
                    binding.nameTextView.text = fullName
                    binding.genderTextView.text = gender
                    binding.heightTextView.text = "Height \n$height"
                    binding.weightTextView.text = "Weight \n$weight"
                    val bmi = calculateBMI(height, weight)
                    binding.imcTextView.text = "BMI \n%.2f".format(bmi)

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
                                        Glide.with(requireContext())
                                            .load(photoUrl)
                                            .placeholder(R.drawable.default_profile_picture) // Placeholder image while loading the photo
                                            .error(R.drawable.default_profile_picture) // Default image in case of error
                                            .into(binding.profileImageView)
                                    }
                                } else {
                                    // If the document does not exist, load the default image from drawable
                                    Glide.with(requireContext())
                                        .load(R.drawable.default_profile_picture)
                                        .into(binding.profileImageView)
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle the error while retrieving the document
                                Toast.makeText(context, "Error retrieving profile photo: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "No user data found.", Toast.LENGTH_SHORT).show()
                }
            }

        // Subscribe to changes in the measurements document
        measurementsDocumentListener = firestore.collection("measurements").document(user.uid)
            .addSnapshotListener { measurementDocument, error ->
                if (error != null) {
                    // Handle the error
                    Toast.makeText(context, "Error retrieving body measurements: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (measurementDocument != null && measurementDocument.exists()) {
                    // Get body measurements
                    val waistMeasurement = measurementDocument.getDouble("waistMeasurement") ?: 0.0
                    val hipMeasurement = measurementDocument.getDouble("hipMeasurement") ?: 0.0
                    val bicepsMeasurement = measurementDocument.getDouble("bicepsMeasurement") ?: 0.0
                    val legMeasurement = measurementDocument.getDouble("legMeasurement") ?: 0.0

                    // Update the UI
                    binding.waistMeasurementTextView.text = "Waist\n$waistMeasurement"
                    binding.hipMeasurementTextView.text = "Hip\n$hipMeasurement"
                    binding.bicepsMeasurementTextView.text = "Biceps\n$bicepsMeasurement"
                    binding.legMeasurementTextView.text = "Legs\n$legMeasurement"
                } else {
                    // If there are no body measurements data, show default values
                    binding.waistMeasurementTextView.text = "Waist\n0.0"
                    binding.hipMeasurementTextView.text = "Hip\n0.0"
                    binding.bicepsMeasurementTextView.text = "Biceps\n0.0"
                    binding.legMeasurementTextView.text = "Legs\n0.0"
                }
            }
    }

    private fun calculateBMI(height: Double, weight: Double): Double {
        val heightInMeters = height / 100 // Convertir la altura de centímetros a metros
        return weight / (heightInMeters * heightInMeters)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Detener la escucha de cambios en el documento del usuario
        userDocumentListener.remove()
        // Detener la escucha de cambios en el documento de las medidas corporales
        measurementsDocumentListener.remove()
    }
}
