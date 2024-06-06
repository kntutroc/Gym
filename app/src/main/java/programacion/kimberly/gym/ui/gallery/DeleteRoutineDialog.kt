package programacion.kimberly.gym.ui.gallery

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteRoutineDialog(private val routineName: String, private val onConfirmDelete: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Are you sure you want to delete the routine \"$routineName\"?")
                setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Usuario confirma la eliminación
                        onConfirmDelete.invoke()
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Usuario cancela la eliminación
                        dialog.dismiss()
                    })
            }
            // Crear el diálogo
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
