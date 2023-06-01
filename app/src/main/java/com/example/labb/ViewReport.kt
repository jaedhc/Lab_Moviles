package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore

class ViewReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_report)
        initApp()
    }

    private fun initApp(){
        val id = this.intent.extras?.getString("id")
        id?.let { getReport(id) }

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
            finish()
        }

        val btnplataformas = findViewById<ImageView>(R.id.btn_plataformas)
        btnplataformas.setOnClickListener{
            val irplataformas = Intent(this, PlataformasActivity::class.java)
            startActivity(irplataformas)
        }

    }

    private fun getReport(id:String){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("Reportes")
        var ingReporte:String
        var balReporte:String
        var gasReporte:String
        var fechaReporte:String


        collection.document(id).get().addOnSuccessListener {
            ingReporte = it.get("ingresos").toString()
            balReporte = it.get("balance").toString()
            gasReporte = it.get("gastos").toString()
            fechaReporte = it.getString("fecha").toString()
            setData(ingReporte, balReporte, gasReporte, fechaReporte)
        }
    }

    private fun setData(ingresos:String, balance:String, gastos:String, fecha:String){
        val txtIngresos = findViewById<EditText>(R.id.edt_ingresos)
        txtIngresos.setText(ingresos)

        val txtBalance = findViewById<EditText>(R.id.edt_balance)
        txtBalance.setText(balance)

        val txtGastos = findViewById<EditText>(R.id.edt_gastos)
        txtGastos.setText(gastos)

        val txtFecha = findViewById<TextView>(R.id.tvFecha)
        txtFecha.setText(fecha)

    }
}