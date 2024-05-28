package programacion.kimberly.gym.ui.gallery

import Routine
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import programacion.kimberly.gym.databinding.FragmentGalleryBinding
import programacion.kimberly.gym.register.MainActivity

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val TAG = "GalleryFragment"

    private lateinit var routinesListener: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val routinesTextView = binding.routinesTextView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val routinesRef = FirebaseFirestore.getInstance().collection("routines").document(uid).collection("userRoutines")

            // Suscribirse a los cambios en la colección de rutinas
            routinesListener = routinesRef.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    // Manejamos el error
                    Log.e(TAG, "Error getting routines: ", error)
                    Toast.makeText(context, "Error getting routines: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val routines = querySnapshot.documents.map { document ->
                        Routine(
                            id = document.id,
                            name = document.getString("name") ?: "Nameless",
                        )
                    }

                    if (routines.isNotEmpty()) {
                        recyclerView.adapter = RoutineAdapter(
                            routines,
                            onDeleteClick = { routine ->
                                routinesRef.document(routine.id).delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Routine removed", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error deleting routine: ", e)
                                        Toast.makeText(context, "Error deleting routine: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            onViewClick = { routine ->
                                val intent = Intent(requireContext(), VerRutina::class.java).apply {
                                    putExtra("ROUTINE_ID", routine.id)
                                }
                                startActivity(intent)
                            }
                        )

                        routinesTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        routinesTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }

            binding.searchExercisesButton.setOnClickListener {
                val intent = Intent(requireContext(), BuscarEjerciciosActivity::class.java)
                startActivity(intent)
            }

            binding.createRoutineButton.setOnClickListener {
                val intent = Intent(requireContext(), CrearRutinaActivity::class.java)
                startActivity(intent)
            }
        } else {
            Log.e(TAG, "Error: User not authenticated")
            Toast.makeText(context, "Error: User not authenticated", Toast.LENGTH_SHORT).show()
            redirectToMainActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Detener la escucha
        routinesListener.remove()
    }

    private fun redirectToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}