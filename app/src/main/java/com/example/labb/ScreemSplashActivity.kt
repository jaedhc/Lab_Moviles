package com.example.labb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class ScreemSplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    val screenSplash = installSplashScreen()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_screem_splash)

    Thread.sleep(1000)
    screenSplash.setKeepOnScreenCondition{ true}
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }
}