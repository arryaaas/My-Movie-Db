package com.arya.mymoviedb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.fragment.findNavController
import com.arya.mymoviedb.R
import com.arya.mymoviedb.database.SharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        updateTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        updateToolbarTheme()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.movieFragment,
                R.id.tvFragment,
                R.id.favFragment
            )
        )

        navHostFragment?.navController?.let {
            NavigationUI.setupActionBarWithNavController(this,
                it, appBarConfiguration)
        }

        navHostFragment?.navController?.let {
            NavigationUI.setupWithNavController(bottomNavigationView,
                it
            )
        }

        val status = intent.getStringExtra("Move")
        if (status == "Favorite") {
            navHostFragment?.findNavController()?.navigate(R.id.action_movieFragment_to_favFragment)
        }
    }

    private fun updateToolbarTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState() == true) {
            toolbar.context.setTheme(R.style.DarkToolbar)
        } else {
            toolbar.context.setTheme(R.style.LightToolbar)
        }
    }

    private fun updateTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }
}
