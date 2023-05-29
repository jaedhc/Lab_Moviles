package com.example.labb.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.ListaUsers
import com.example.labb.R

class UsersHolder(view: View, listener: UsersAdapter.onItemClickListener): RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.txt_name)
    val email: TextView = view.findViewById(R.id.txt_email)

    init {
        itemView.setOnClickListener{
            listener.onItemClick(adapterPosition)
        }
    }

    fun render (users: ListaUsers){
        name.text = users.nameUser
        email.text = users.emailUser
    }


}