package com.example.labb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.ListaReports
import com.example.labb.ListaUsers
import com.example.labb.R
import com.example.labb.listas.ListaChats

class ChatAdapter(val chatClick: (ListaChats) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    var chats: List<ListaChats> = emptyList()

    fun setData(list: List<ListaChats>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_chats,
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.txt_nombre).text = "fort"
        holder.itemView.findViewById<TextView>(R.id.txt_correo).text = "fort@gmail.com"

        holder.itemView.setOnClickListener {
            chatClick(chats[position])
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}