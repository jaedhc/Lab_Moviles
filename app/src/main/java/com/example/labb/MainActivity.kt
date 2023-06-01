package com.example.labb

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    val pickImageCode = 123
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUserConfig()

        auth = Firebase.auth
        val currentuser = auth.currentUser
        if(currentuser==null){
            callInicioSesion()
        } else {
            currentuser.email?.let {
                init(it)
                getname(it)
                getBalance(it)
            }
        }

        val imgUser = findViewById<ImageView>(R.id.image_user)
        imgUser.setOnClickListener{
            pickImage()
        }

        val btnchat = findViewById<Button>(R.id.btnchat)
        btnchat.setOnClickListener{
            val irchat = Intent(this, ChatActivity::class.java)
            startActivity(irchat)
        }

        val btnhome = findViewById<ImageView>(R.id.btn_home)
        btnhome.setOnClickListener{
            val irhome = Intent(this, MainActivity::class.java)
            startActivity(irhome)
        }

        val btnmetas = findViewById<ImageView>(R.id.btn_metas)
        btnmetas.setOnClickListener{
            val irmetas = Intent(this, MetasActivity::class.java)
            startActivity(irmetas)
        }

        val btnpresupuesto = findViewById<ImageView>(R.id.btn_presupuesto)
        btnpresupuesto.setOnClickListener{
            val irpresupuesto = Intent(this, PresupuestoActivity::class.java)
            startActivity(irpresupuesto)
        }

        val btnplataformas = findViewById<ImageView>(R.id.btn_plataformas)
        btnplataformas.setOnClickListener{
            val irplataformas = Intent(this, PlataformasActivity::class.java)
            startActivity(irplataformas)
        }



    }

    private fun pickImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImageCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageCode && resultCode == RESULT_OK){
            val sharedPref = applicationContext.getSharedPreferences("user", 0)
            val email = auth.currentUser?.email.toString()
            val edit = sharedPref.edit()

            imageUri = data?.data
            val storageRef = FirebaseStorage.getInstance().reference
            val imagesRef = storageRef.child("fotos").child(email.toString())

            imagesRef.putFile(imageUri!!).addOnSuccessListener { it->
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadImg = uri.toString()
                    sharedPref.edit().putString("prefUrl", downloadImg).commit()
                    edit.apply()
                    setUserConfig()
                }.addOnFailureListener{ Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show() }
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUserConfig(){
        //SHARED PREFERENCES
        val sharedPref = applicationContext.getSharedPreferences("user", 0)
        val url = sharedPref.getString("prefUrl", R.string.def_url_img.toString())

        //USER IMAGE
        val imgUser = findViewById<ImageView>(R.id.image_user)
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.ic_baseline_person_24)
            .into(imgUser)



    }

    private fun getname(correo: String){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Users")
        collection.whereEqualTo("correo",correo).get().addOnSuccessListener {
            for (doc in it) {
                val nombre = doc.getString("nombre").toString()
                actualizarnombre(nombre)
            }
        }
    }

    private fun getBalance(correo: String){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Reportes")
        var sumatoria:Float = 0f
        var sumin:Float = 0f
        var sumgas:Float = 0f
        collection.whereEqualTo("correo",correo).get().addOnSuccessListener {
            for (doc in it) {
                sumatoria += doc.get("balance").toString().toFloat()
                sumin += doc.get("ingresos").toString().toFloat()
                sumgas += doc.get("gastos").toString().toFloat()
            }
            val txt_balance:TextView = findViewById(R.id.txt_ahorros)
            txt_balance.text = "$$sumatoria"
            val txt_ingresos:TextView = findViewById(R.id.txt_ingresos)
            txt_ingresos.text = "$$sumin"
            val txt_gastos:TextView = findViewById(R.id.txt_gastos)
            txt_gastos.text = "$$sumgas"
        }


    }

    private fun actualizarnombre(nombre: String){
        val txtnombre = findViewById<TextView>(R.id.NombreUsuario)
        txtnombre.text = nombre

    }

    private fun init(correo : String){
        val db = FirebaseFirestore.getInstance()
        val btnmanage = findViewById<Button>(R.id.btn_admin)
        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar)

        btnCerrarSesion.setOnClickListener{
            borrarPreferences()
            val iniciarSesion = Intent(this, LoginActivity::class.java)
            startActivity(iniciarSesion)
            finish()
        }
        btnmanage.setOnClickListener{
            calladmin()
        }
        db.collection("Users").whereEqualTo("correo", correo).get().addOnSuccessListener {
            it.forEach{
                if(it.getString("type").toString().equals("admin")){
                    btnmanage.visibility = View.VISIBLE
                }else{
                    btnmanage.visibility = View.GONE
                }
            }
        }
    }

    private fun borrarPreferences(){
        val sharedPref = applicationContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        val edit = sharedPref?.edit()
        edit?.putString("prefUser", "Usuario")?.commit()
        edit?.putString("prefUrl", R.string.def_url_img.toString())?.commit()
        auth.signOut()
        edit?.apply()
    }

    private fun calladmin(){
        val sesion = Intent(this, AdminActivity::class.java)
        startActivity(sesion)
    }

    private fun callInicioSesion(){
        val sesion = Intent(this, LoginActivity::class.java)
        startActivity(sesion)
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}

