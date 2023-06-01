package com.example.labb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mBtn: Button
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser != null){
            callMain()
        }

        val btnRegistro:TextView = findViewById(R.id.txt_registro)
        val correo:EditText = findViewById(R.id.edt_usuario)
        val contrasenia:EditText = findViewById(R.id.edt_contrasenia)
        val btnInicio:Button = findViewById(R.id.btn_completados)
        mBtn = findViewById(R.id.idioma)
        mBtn.setOnClickListener(){
            showIdioma()
        }


        btnRegistro.setOnClickListener{
            val registro = Intent(this, SigninActivity::class.java)
            startActivity(registro)
        }

        btnInicio.setOnClickListener{
            validarDatos(correo.text.toString(), contrasenia.text.toString())
        }
    }

    private fun showIdioma() {
        val listItem = arrayOf("Español", "Ingles")
        val mBuilder = AlertDialog.Builder(this@LoginActivity)
        mBuilder.setTitle("Idioma")
        mBuilder.setSingleChoiceItems(listItem, -1){ dialog, which ->
            if (which == 0) {
                setLocate("es")
                recreate()
            }else if (which == 1) {
                setLocate("en")
                recreate()
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()

    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val len = sharedPreferences.getString("My_Lang", "")
        if (len != null) {
            setLocate(len)
        }
    }

    private fun validarDatos(correo:String, contrasenia:String){
        var error = false
        if(correo.equals(" ") || validateEmail(correo) === false){
            error = true
        }
        if(contrasenia.equals(" ")){
            error = true
        }

        if(error == false) {
            iniciarSesion(correo, contrasenia)
        } else {
            Toast.makeText(this, "Ingrese Datos completos", Toast.LENGTH_LONG).show()
        }
    }

    private fun iniciarSesion(correo:String, contrasenia:String) {
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPref?.edit()
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(correo, contrasenia).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val usuario = Firebase.auth.currentUser
                val verifica = usuario?.isEmailVerified
                if (verifica == true){
                    edit?.putString("prefUser", correo)?.commit()
                    getImage(correo)
                    edit?.apply()

                } else {
                Toast.makeText(this, "No ha verificado su correo", Toast.LENGTH_LONG).show()
            }
            }else{
                Toast.makeText(this, "El usuario o contraseña son equivocados", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun getImage(email:String){
        var imagesRef = storageRef.child("fotos").child(email)
        var url = "https://firebasestorage.googleapis.com/v0/b/jairohw-f9dc0.appspot.com/o/fotos%2Fuser.png?alt=media&token=0cd8afb8-0dc5-48b3-942a-2eaf529d82b8"
        imagesRef.downloadUrl.addOnSuccessListener {
            url = it.toString()
            val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
            var edit = sharedPref?.edit()
            sharedPref?.edit()?.putString("prefUrl", url)?.commit()
            edit?.apply()
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener {
            val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
            var edit = sharedPref?.edit()
            sharedPref?.edit()?.putString("prefUrl", url)?.commit()
            edit?.apply()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }



    private fun validateEmail(email:String):Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun callMain(){
        val main = Intent(this, MainActivity::class.java)
        startActivity(main)
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}