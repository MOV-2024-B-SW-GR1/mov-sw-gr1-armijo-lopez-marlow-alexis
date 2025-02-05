package com.example.sw2024bgr1_maal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AtletaAdapter(
    private var atletas: List<Atleta>,
    private val listener: AtletaClickListener
) : RecyclerView.Adapter<AtletaAdapter.AtletaViewHolder>() {

    interface AtletaClickListener {
        fun onAtletaClick(atleta: Atleta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_atleta, parent, false)
        return AtletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtletaViewHolder, position: Int) {
        val atleta = atletas[position]
        holder.bind(atleta)
        holder.itemView.setOnClickListener { listener.onAtletaClick(atleta) }
    }

    override fun getItemCount() = atletas.size

    fun updateAtletas(newAtletas: List<Atleta>) {
        atletas = newAtletas
        notifyDataSetChanged()
    }

    class AtletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreAtleta)
        private val tvEdad: TextView = itemView.findViewById(R.id.tvEdadAtleta)
        private val tvMejorMarca: TextView = itemView.findViewById(R.id.tvMejorMarcaAtleta)
        private val tvRegistrado: TextView = itemView.findViewById(R.id.tvRegistradoAtleta)

        fun bind(atleta: Atleta) {
            tvNombre.text = atleta.nombre
            tvEdad.text = "Edad: ${atleta.edad}"
            tvMejorMarca.text = "Mejor Marca: ${atleta.mejorMarca}"
            tvRegistrado.text = "Registrado: ${if (atleta.estaRegistrado) "Sí" else "No"}"
        }
    }
}