package com.example.labb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ModifyUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_user)
        initApp()
    }
    private fun initApp(){
        initUI()

        /*setSpinner()*/

        val id = this.intent.extras?.getString("id")
        id?.let { getUser(it) }

        val btnGuardar = findViewById<Button>(R.id.btn_Save)

        val txtName = findViewById<EditText>(R.id.input_name)
        val txttype = findViewById<EditText>(R.id.input_type)

        btnGuardar.setOnClickListener {
            val name = txtName.text.toString()
            val type = txttype.text.toString()
            id?.let { modifyUser(it, name, type)
            }
        }

    }

    private fun initUI(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val view: View = window.decorView.findViewById(android.R.id.content)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        /*Insetter.builder()
            .marginBottom(windowInsetTypesOf(navigationBars = true))
            .applyToView(view) */
    }


    private fun modifyUser(id:String, name:String, type: String){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Users")

        collection.document(id).get().addOnSuccessListener {
            it.reference.update("nombre", name)
            it.reference.update("type", type)
            Toast.makeText(this, "Cambios guardados con Exito", Toast.LENGTH_LONG).show()
            callManage()
        }.addOnFailureListener{
            Toast.makeText(this, "ERROOOR", Toast.LENGTH_LONG).show()
        }
    }

    private fun getUser(id:String){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Users")
        var nameUser:String
        var emailUser:String
        var typeUser:String


        collection.document(id).get().addOnSuccessListener {
            nameUser = it.getString("nombre").toString()
            emailUser = it.getString("correo").toString()
            typeUser = it.getString("type").toString()
            setData(nameUser, emailUser, typeUser)
        }
    }

    /*
    private fun setSpinner(){
        val types = resources.getStringArray(R.array.user_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_type, types)

        val spinner: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        spinner.keyListener = null

        spinner.setAdapter(arrayAdapter)
    } */

    private fun setData(name:String, email:String, type:String){
        val txtEmail = findViewById<EditText>(R.id.input_email)
        txtEmail.setText(email)

        val txtName = findViewById<EditText>(R.id.input_name)
        txtName.setText(name)

        val txttype = findViewById<EditText>(R.id.input_type)
        txttype.setText(type)
    }


    private fun callManage(){
        finish()
    }
}