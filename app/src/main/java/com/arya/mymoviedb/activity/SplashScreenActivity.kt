package com.arya.mymoviedb.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arya.mymoviedb.R
import com.arya.mymoviedb.database.SharedPref

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }, 2000)

        val stb = AnimationUtils.loadAnimation(this, R.anim.stb)
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        val appLogo = findViewById<ImageView>(R.id.logo)
        val appName = findViewById<TextView>(R.id.app_name)
        val text = findViewById<ConstraintLayout>(R.id.text)

        text.visibility = View.VISIBLE

        appLogo.startAnimation(stb)
        appName.startAnimation(btt)
    }
}
