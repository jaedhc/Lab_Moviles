package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.adapter.ChatAdapter
import com.example.labb.listas.ListaChats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class TextChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_chat)

        auth = Firebase.auth
        val currentuser = auth.currentUser

        if(currentuser != null){
            init(currentuser.email.toString())
        }

    }

    private fun init(email:String){
        val back = findViewById<ImageView>(R.id.btn_back)
        back.setOnClickListener{
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
        }

        getChats(email)

    }

    private fun newChat(correo: String) {
        val newChatText = findViewById<EditText>(R.id.edt_buscador)
        val chatId = UUID.randomUUID().toString()
        val otherUser = newChatText.text.toString()

        val users = listOf(getIDs(correo), getIDs(otherUser))

        val chat = ListaChats(
            idChat = chatId,
            nameChat = "Chat con $otherUser",
            usersChat = users
        )
    }

    private fun getIDs(correo:String):String{
        var userRef = db.collection("Users")
        var id = ""
        userRef.whereEqualTo("correo", correo).get().addOnSuccessListener {
            for (document in it){
                id = document.id
                //return id
            }
        }

        return id
    }

    private fun getChats(correo: String){
        var id = ""
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_chats)
        val txt = findViewById<TextView>(R.id.txt_Title)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ChatAdapter{
            chat -> chatSelected(chat)
        }

        var userRef = db.collection("Users")

        userRef.whereEqualTo("correo", correo).get().addOnSuccessListener {
            for (document in it){
                id = document.id

                val userRef2 = db.collection("Users").document(id)

                userRef2.collection("Chats").get().addOnSuccessListener {
                        chats ->
                    val listaChats = chats.toObjects(ListaChats::class.java)

                    (recyclerView.adapter as ChatAdapter).setData(listaChats)
                }

                userRef2.collection("Chats").addSnapshotListener { chats, error ->
                    if(error == null){
                        chats?.let {
                            val listChats = it.toObjects(ListaChats::class.java)

                            (recyclerView.adapter as ChatAdapter).setData(listChats)
                        }
                    }
                }
            }
        }



        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Users")
        collection.whereEqualTo("correo",correo).get().addOnSuccessListener {
            for (doc in it) {
                val nombre = doc.getString("nombre").toString()
                //actualizarnombre(nombre)
            }
        }
    }

    private fun chatSelected(chat: ListaChats){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.idChat)
        intent.putExtra("chatName", chat.nameChat)
        startActivity(intent)
    }
}