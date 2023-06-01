package com.example.labb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.ListaReports
import com.example.labb.R
import com.google.firebase.firestore.util.Listener
import java.text.FieldPosition

class ReporteAdapter(private val reportes: List<ListaReports>): RecyclerView.Adapter<ReporteHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReporteHolder(layoutInflater.inflate(R.layout.list_report, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ReporteHolder, position: Int) {
        holder.render(reportes[position])
    }

    override fun getItemCount(): Int = reportes.size
}