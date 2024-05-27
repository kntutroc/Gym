package programacion.kimberly.gym.ui.slideshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import programacion.kimberly.gym.R


class ExerciseAdapter(private val exercises: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exerciseNameTextView: TextView = view.findViewById(R.id.exerciseNameTextView)
        val exerciseImageView: ImageView = view.findViewById(R.id.exerciseImageView)
        val exerciseTargetTextView: TextView = view.findViewById(R.id.exerciseTargetTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameTextView.text = exercise.name
        holder.exerciseTargetTextView.text = exercise.target
        Glide.with(holder.itemView.context)
            .load(exercise.gifUrl)
            .into(holder.exerciseImageView)
    }

    data class Exercise(
        val id: String,
        val name: String,
        val gifUrl: String,
        val target: String
    )
    override fun getItemCount(): Int = exercises.size
}