package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.adapter.UsersAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
    }
    override fun onResume() {
        super.onResume()
        initApp()
    }

    private fun initApp(){
        initUI()
        getUsers()
    }

    private fun initUI(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val view: View = window.decorView.findViewById(android.R.id.content)

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }


    private fun getUsers(){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Users")
        var nameUser:String
        var emailUser:String
        var documentUser:String
        val userList: MutableList<ListaUsers> = mutableListOf()


        collection.get().addOnSuccessListener {
            for (document in it){
                nameUser = document.getString("nombre").toString()
                emailUser = document.getString("correo").toString()
                documentUser = document.id
                userList.add(ListaUsers(nameUser, emailUser, documentUser))
                initRecyclerView(userList)
            }
        }
    }


    private fun initRecyclerView(users: List<ListaUsers>){
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = UsersAdapter(users)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object : UsersAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                callModifyUser(users[position].idUser)
            }
        })
    }

    private fun callModifyUser(id:String){
        val modify = Intent(this, ModifyUser::class.java)
        modify.putExtra("id", id)
        startActivity(modify)
    }
}