package programacion.kimberly.gym.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import programacion.kimberly.gym.databinding.FragmentSlideshowBinding
import programacion.kimberly.gym.ui.slideshow.type.BeginnerActivity
import programacion.kimberly.gym.ui.slideshow.type.IntermediateActivity
import programacion.kimberly.gym.ui.slideshow.type.AdvancedActivity
import kotlin.math.roundToInt

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set the on click listeners for the CardViews
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

    // Extension function to convert dp to px
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).roundToInt()
    }
}