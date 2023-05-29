package com.example.labb

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val correo: EditText = findViewById(R.id.edt_usuario)
        val pass: EditText = findViewById(R.id.edt_contrasenia)
        val passRep: EditText = findViewById(R.id.edt_contrasenia_rep)
        val texto:TextView = findViewById(R.id.textView)
        val nombre: EditText = findViewById(R.id.edt_nombre)

        val btnRegistro: Button = findViewById(R.id.btn_completados)


        btnRegistro.setOnClickListener{
            validarDatos(nombre.text.toString(), correo.text.toString(), pass.text.toString(), passRep.text.toString())
        }
    }


    private fun validarDatos(nombre:String, correo:String, contrasenia:String, contraseniaRep: String){
        var error = false
        if(nombre.isEmpty() || correo.isEmpty() || contrasenia.isEmpty() || contraseniaRep.isEmpty()){
            Toast.makeText(this, "Verifique todos los campos", Toast.LENGTH_LONG).show()
            error = true
        }else if(!validateEmail(correo)){
            Toast.makeText(this, "Verifique el campo de correo", Toast.LENGTH_LONG).show()
            error = true
        }else if(!contraseniaRep.equals(contrasenia)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            error = true
        }
        if(error == false) {
            crearUsuario(correo, contrasenia, nombre)
            callInicioSesion()
        }
    }


    private fun crearUsuario(correo:String, contrasenia:String, nombre: String){
        auth = Firebase.auth
        val hashMap = hashMapOf("nombre" to nombre, "correo" to correo, "type" to "normal")
        val db = FirebaseFirestore.getInstance()
        val id = System.currentTimeMillis().toInt()

        auth.createUserWithEmailAndPassword(correo, contrasenia).addOnCompleteListener {
            if(it.isSuccessful){
                db.collection("Users").document(id.toString()).set(hashMap, SetOptions.merge()).addOnCompleteListener {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    sendEmailverif()
                    Toast.makeText(this, "Registrado exitosamente, Requiere de Verificacion", Toast.LENGTH_LONG).show()
                }
            } else {
                Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callInicioSesion(){
        val inicio = Intent(this, LoginActivity::class.java)
        startActivity(inicio)
    }

    private fun validateEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    private fun sendEmailverif(){
        val user = Firebase.auth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){ task ->
            if (task.isSuccessful)
            {

            }
            else
            {

            }
        }
    }
}