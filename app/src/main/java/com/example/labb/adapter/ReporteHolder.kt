package com.example.labb.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.ListaReports
import com.example.labb.R

class ReporteHolder(view: View, listener: ReporteAdapter.onItemClickListener): RecyclerView.ViewHolder(view) {

    val ing: TextView = view.findViewById(R.id.txt_ingresos)
    val bal: TextView = view.findViewById(R.id.txt_balance)
    val fecha: TextView = view.findViewById(R.id.txt_fecha)

    init {
        itemView.setOnClickListener{
            listener.onItemClick(adapterPosition)
        }
    }

    fun render (reports: ListaReports){
        ing.text = "Ingresos: " + reports.ingReporte
        bal.text = "Balance: " + reports.balReporte
        fecha.text = reports.fechaReporte
    }


}