package DEBERIIB

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sw2024bgr1_maal.R

class AtletaAdapter(
    private val context: Context,
    private val atletas: List<Atleta>,
    private val onItemClicked: (Atleta) -> Unit
) : RecyclerView.Adapter<AtletaAdapter.AtletaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_atleta, parent, false)
        return AtletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtletaViewHolder, position: Int) {
        val atleta = atletas[position]
        holder.bind(atleta)
        holder.itemView.setOnClickListener { onItemClicked(atleta) }
    }

    override fun getItemCount(): Int = atletas.size

    inner class AtletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreAtleta)
        private val tvEdad: TextView = itemView.findViewById(R.id.tvEdadAtleta)

        fun bind(atleta: Atleta) {
            tvNombre.text = atleta.nombre
            tvEdad.text = "Edad: ${atleta.edad}"
        }
    }
}
