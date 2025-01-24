package com.example.sw2024bgr1_maal

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment

class dialog_atleta_options : DialogFragment() {
    private var listener: AtletaOptionsListener? = null

    interface AtletaOptionsListener {
        fun onEditarClick()
        fun onEliminarClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val opciones = arrayOf("Editar", "Eliminar")

        return AlertDialog.Builder(requireContext())
            .setTitle("Opciones de Atleta")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> listener?.onEditarClick()
                    1 -> listener?.onEliminarClick()
                }
            }
            .setNegativeButton("Cancelar") { _, _ ->
                dismiss()
            }
            .create()
    }

    fun setAtletaOptionsListener(listener: AtletaOptionsListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogAtletaOptions"

        fun newInstance(): dialog_atleta_options {
            return dialog_atleta_options()
        }
    }
}