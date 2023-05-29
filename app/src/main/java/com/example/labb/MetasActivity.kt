package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MetasActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.metas_metas)


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
}