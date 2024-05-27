package programacion.kimberly.gym.ui.gallery

import Routine
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import programacion.kimberly.gym.R

class RoutineAdapter(
    private val routines: List<Routine>,
    private val onDeleteClick: (Routine) -> Unit,
    private val onViewClick: (Routine) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Inicializamos los views
        val routineNameTextView: TextView = itemView.findViewById(R.id.routineNameTextView)
        val deleteRoutineButton: ImageButton = itemView.findViewById(R.id.deleteRoutineButton)
        val viewRoutineButton: Button = itemView.findViewById(R.id.viewRoutineButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val currentRoutine = routines[position]
        holder.routineNameTextView.text = currentRoutine.name

        holder.deleteRoutineButton.setOnClickListener {
            onDeleteClick(currentRoutine)
        }

        holder.viewRoutineButton.setOnClickListener {
            onViewClick(currentRoutine)
        }
    }

    override fun getItemCount() = routines.size
}