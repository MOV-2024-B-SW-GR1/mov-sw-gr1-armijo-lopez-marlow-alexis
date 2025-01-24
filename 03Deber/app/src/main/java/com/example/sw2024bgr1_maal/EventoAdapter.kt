package com.example.sw2024bgr1_maal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventoAdapter(
    private var eventos: List<Evento>,
    private val listener: EventoClickListener
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    interface EventoClickListener {
        fun onEventoClick(evento: Evento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.bind(evento)
        holder.itemView.setOnClickListener { listener.onEventoClick(evento) }
    }

    override fun getItemCount() = eventos.size

    fun updateEventos(newEventos: List<Evento>) {
        eventos = newEventos
        notifyDataSetChanged()
    }

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEvento)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)
        private val tvCapacidad: TextView = itemView.findViewById(R.id.tvCapacidadEvento)
        private val tvCuota: TextView = itemView.findViewById(R.id.tvCuotaEvento)

        fun bind(evento: Evento) {
            tvNombre.text = evento.nombre
            tvFecha.text = "Fecha: ${evento.fecha}"
            tvCapacidad.text = "Capacidad: ${evento.capacidad}"
            tvCuota.text = "Cuota: $${evento.cuota}"
        }
    }
}
