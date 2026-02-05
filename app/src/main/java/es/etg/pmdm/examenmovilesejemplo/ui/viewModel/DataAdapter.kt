package es.etg.pmdm.examenmovilesejemplo.ui.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.etg.pmdm.examenmovilesejemplo.R

class DataAdapter(private var mList: List<ItemViewModel>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    // Función para actualizar los datos
    fun updateData(newList: List<ItemViewModel>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarjeta_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        holder.nombrePokemon.text = itemViewModel.nombre
        holder.urlPokemon.text = itemViewModel.url
    }

    override fun getItemCount(): Int = mList.size

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nombrePokemon: TextView = itemView.findViewById(R.id.txtNombrePokemon)
        val urlPokemon: TextView = itemView.findViewById(R.id.txtUrlPokemon)
    }
}