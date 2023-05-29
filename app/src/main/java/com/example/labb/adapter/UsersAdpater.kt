package com.example.labb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.ListaUsers
import com.example.labb.R
import com.google.firebase.firestore.util.Listener
import java.text.FieldPosition

class UsersAdapter(private val users: List<ListaUsers>): RecyclerView.Adapter<UsersHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsersHolder(layoutInflater.inflate(R.layout.list_user, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        holder.render(users[position])
    }

    override fun getItemCount(): Int = users.size
}