package com.example.labb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
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

