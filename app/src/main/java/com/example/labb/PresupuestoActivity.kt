package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class PresupuestoActivity : AppCompatActivity() {
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_presupuesto)

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

    val btnbalance = findViewById<Button>(R.id.btn_balance)
    btnbalance.setOnClickListener{
      CalcularBalance()
    }

    val btn_add = findViewById<CardView>(R.id.btn_add)
    btn_add.setOnClickListener{
      val edt_ingresos:EditText = findViewById(R.id.edt_ingresos)
      val edt_gastos:EditText = findViewById(R.id.edt_gastos)
      val edt_balance:EditText = findViewById(R.id.edt_balance)
      val ingresos = edt_ingresos.text.toString().toFloat()
      val gastos = edt_gastos.text.toString().toFloat()
      val balance = edt_balance.text.toString().toFloat()
      SubirBalance(ingresos, gastos, balance)
    }

  }

  private fun CalcularBalance(){
    val edt_ingresos:EditText = findViewById(R.id.edt_ingresos)
    val edt_gastos:EditText = findViewById(R.id.edt_gastos)
    val edt_balance:EditText = findViewById(R.id.edt_balance)
    val ingresos = edt_ingresos.text.toString().toFloat()
    val gastos = edt_gastos.text.toString().toFloat()
    val balance = ingresos-gastos
    edt_balance.setText(balance.toString())
  }

  private fun SubirBalance(ingresos:Float, gastos:Float, balance:Float){
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("Reportes")
    var correo:String = ""
    val id = System.currentTimeMillis().toInt()

    auth = Firebase.auth
    val currentuser = auth.currentUser
    if(currentuser==null){
    } else {
      currentuser.email?.let { correo = it }
    }
    val hashMap = hashMapOf("ingresos" to ingresos, "gastos" to gastos, "balance" to balance, "correo" to correo)
    collection.document(id.toString()).set(hashMap, SetOptions.merge()).addOnSuccessListener {
      Toast.makeText(
        this,
        "Con Exito",
        Toast.LENGTH_SHORT
      ).show() }

  }

}