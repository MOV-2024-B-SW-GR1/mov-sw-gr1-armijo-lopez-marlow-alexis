package DEBERIIB

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sw2024bgr1_maal.R

// Adapter para eventos
class EventoAdapter(
    private val context: Context,
    private val eventos: List<Evento>,
    private val onItemClicked: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.bind(evento)
        holder.itemView.setOnClickListener { onItemClicked(evento) }
    }

    override fun getItemCount(): Int = eventos.size

    inner class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEvento)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)

        fun bind(evento: Evento) {
            tvNombre.text = evento.nombre
            tvFecha.text = evento.fecha
        }
    }
}
