package com.example.sw2024bgr1_maal

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment

class dialog_confirm_delete :  DialogFragment() {
    private var listener: ConfirmDeleteListener? = null

    interface ConfirmDeleteListener {
        fun onConfirmDelete()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Está seguro de que desea eliminar este registro?")
            .setPositiveButton("Confirmar") { _, _ ->
                listener?.onConfirmDelete()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    fun setConfirmDeleteListener(listener: ConfirmDeleteListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogConfirmDelete"

        fun newInstance(): dialog_confirm_delete {
            return dialog_confirm_delete()
        }
    }
}