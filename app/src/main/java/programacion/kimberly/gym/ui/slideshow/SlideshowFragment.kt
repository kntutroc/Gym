package programacion.kimberly.gym.ui.slideshow

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import programacion.kimberly.gym.R
import programacion.kimberly.gym.databinding.FragmentSlideshowBinding
import programacion.kimberly.gym.register.MainActivity
import programacion.kimberly.gym.ui.slideshow.type.BeginnerActivity
import programacion.kimberly.gym.ui.slideshow.type.IntermediateActivity
import programacion.kimberly.gym.ui.slideshow.type.AdvancedActivity
import kotlin.math.roundToInt


class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mostrar el diálogo con indicaciones y advertencias
        showInstructionsDialog()

        // Verificar el estado del usuario
        checkUserStatus()

        // Asignamos los listener a cada intent
        binding.beginnerCardView.setOnClickListener {
            val intent = Intent(requireContext(), BeginnerActivity::class.java)
            startActivity(intent)
        }

        binding.intermediateCardView.setOnClickListener {
            val intent = Intent(requireContext(), IntermediateActivity::class.java)
            startActivity(intent)
        }

        binding.advancedCardView.setOnClickListener {
            val intent = Intent(requireContext(), AdvancedActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Convertir dp to px
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).roundToInt()
    }

    // Mostrar el diálogo con indicaciones y advertencias
    private fun showInstructionsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_title))
        builder.setMessage(getString(R.string.dialog_message_routines))
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    // Verificar el estado del usuario
    private fun checkUserStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Si no hay usuario autenticado, redirigir al MainActivity
            redirectToMainActivity()
            return
        }

        // Suscripción a cambios en el documento del usuario
        firestore.collection("users").document(user.uid)
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
    }

    // Redirigir al MainActivity
    private fun redirectToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}