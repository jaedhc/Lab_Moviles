package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labb.adapter.ReporteAdapter
import com.example.labb.adapter.UsersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MetasActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.metas_metas)

    auth = Firebase.auth
    val currentuser = auth.currentUser

    if(currentuser==null){
      callInicioSesion()
    } else {
      getReportes(auth.currentUser?.email.toString())
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

  private fun getReportes(email: String){
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("Reportes")
    var ingresoReporte = "0.0"
    var balanceReporte = "0.0"
    var ingresoFecha:String
    var documentReporte:String
    val reporteList: MutableList<ListaReports> = mutableListOf()


    collection.whereEqualTo("correo", email).get().addOnSuccessListener {
      for (document in it){
        ingresoReporte = document.get("ingresos").toString()
        balanceReporte = document.get("balance").toString()
        ingresoFecha = "25/7/23"//document.getString("correo").toString()
        documentReporte = document.id
        print("fort")
        reporteList.add(ListaReports(ingresoReporte, balanceReporte, ingresoFecha, documentReporte))
        initRecyclerView(reporteList)
      }
    }
  }

  private fun initRecyclerView(reportes: List<ListaReports>){
    val recyclerView = findViewById<RecyclerView>(R.id.recycleView_reportes)
    recyclerView.layoutManager = LinearLayoutManager(this)
    val adapter = ReporteAdapter(reportes)
    recyclerView.adapter = adapter


    adapter.setOnClickListener(object : ReporteAdapter.onItemClickListener{
      override fun onItemClick(position: Int) {
        callViewReport(reportes[position].idReporte)
      }
    })
  }

  private fun callViewReport(id:String){
    val view = Intent(this, ViewReport::class.java)
    view.putExtra("id", id)
    startActivity(view)
  }

  private fun callInicioSesion(){
    val sesion = Intent(this, LoginActivity::class.java)
    startActivity(sesion)
    finish()
  }
}