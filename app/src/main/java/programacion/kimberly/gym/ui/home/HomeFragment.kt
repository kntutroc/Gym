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
import programacion.kimberly.gym.register.MainActivity
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

        // Obtener el usuario actual
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Si no hay usuario autenticado, redirigir al MainActivity
            redirectToMainActivity()
            return
        }

        // Suscripción a cambios en el documento del usuario
        userDocumentListener = firestore.collection("users").document(user.uid)
            .addSnapshotListener { userDocument, error ->
                if (error != null) {
                    // Manejar el error
                    return@addSnapshotListener
                }

                if (userDocument != null) {
                    // Verificar si el usuario está deshabilitado
                    val isDisabled = userDocument.getBoolean("disabled") ?: false
                    if (isDisabled) {
                        // Si el usuario está deshabilitado, cerrar sesión y redirigir al MainActivity
                        FirebaseAuth.getInstance().signOut()
                        redirectToMainActivity()
                    }
                }
            }

        // Bindeamos los botones
        binding.expandOptionsButton.setOnClickListener {
            startActivity(Intent(activity, PesoActivity::class.java))
        }

        binding.updateMeasurementsButton.setOnClickListener {
            startActivity(Intent(activity, MedidasActivity::class.java))
        }

        // Nos suscribimos a los cambios
        userDocumentListener = firestore.collection("users").document(user.uid)
            .addSnapshotListener { userDocument, error ->
                if (error != null) {
                    // Manejamos el error
                    Toast.makeText(context, "Error retrieving user data: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (userDocument != null && userDocument.exists()) {
                    // Obtenemos los datos
                    val firstName = userDocument.getString("firstName") ?: ""
                    val lastName = userDocument.getString("lastName") ?: ""
                    val fullName = "$firstName $lastName"
                    val gender = userDocument.getString("gender") ?: "Gender not available"
                    val height = userDocument.getDouble("height") ?: 0.0
                    val weight = userDocument.getDouble("weight") ?: 0.0

                    // Updateamos UI con los datos
                    binding.nameTextView.text = fullName
                    binding.genderTextView.text = gender
                    binding.heightTextView.text = "Height \n$height"
                    binding.weightTextView.text = "Weight \n$weight"
                    val bmi = calculateBMI(height, weight)
                    binding.imcTextView.text = "BMI \n%.2f".format(bmi)

                    // Cargamos la foto de perfil
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId != null) {
                        val profilePhotoRef = firestore.collection("profile_photos").document(userId)

                        profilePhotoRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    // Cargamos con Glide la foto
                                    val photoUrl = documentSnapshot.getString("photoUrl")
                                    if (photoUrl != null) {
                                        Glide.with(requireContext())
                                            .load(photoUrl)
                                            .placeholder(R.drawable.default_profile_picture)
                                            .error(R.drawable.default_profile_picture) // Default image en caso deerror
                                            .into(binding.profileImageView)
                                    }
                                } else {
                                    // Si no existe el documento carga la foto por defecto
                                    Glide.with(requireContext())
                                        .load(R.drawable.default_profile_picture)
                                        .into(binding.profileImageView)
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error retrieving profile photo: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "No user data found.", Toast.LENGTH_SHORT).show()
                }
            }

        // Nos suscribimos a los datos
        measurementsDocumentListener = firestore.collection("measurements").document(user.uid)
            .addSnapshotListener { measurementDocument, error ->
                if (error != null) {
                    // Manejamos error
                    Toast.makeText(context, "Error retrieving body measurements: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (measurementDocument != null && measurementDocument.exists()) {

                    val waistMeasurement = measurementDocument.getDouble("waistMeasurement") ?: 0.0
                    val hipMeasurement = measurementDocument.getDouble("hipMeasurement") ?: 0.0
                    val bicepsMeasurement = measurementDocument.getDouble("bicepsMeasurement") ?: 0.0
                    val legMeasurement = measurementDocument.getDouble("legMeasurement") ?: 0.0


                    binding.waistMeasurementTextView.text = "Waist\n$waistMeasurement"
                    binding.hipMeasurementTextView.text = "Hip\n$hipMeasurement"
                    binding.bicepsMeasurementTextView.text = "Biceps\n$bicepsMeasurement"
                    binding.legMeasurementTextView.text = "Legs\n$legMeasurement"
                } else {

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

    private fun redirectToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
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
