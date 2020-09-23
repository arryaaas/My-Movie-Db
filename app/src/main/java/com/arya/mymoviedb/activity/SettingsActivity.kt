package com.arya.mymoviedb.activity

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arya.mymoviedb.reminder.AlarmReceiver
import com.arya.mymoviedb.R
import com.arya.mymoviedb.database.SharedPref
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.toolbar_settings))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Settings"

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 0)
        }

        if (sharedPref.loadNightModeState() == true) {
            theme_switch.isChecked = true
        }

        theme_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref.setNightModeState(true)
                restartApp()
            } else {
                sharedPref.setNightModeState(false)
                restartApp()
            }
        }

        if (sharedPref.loadDailyState() == true) {
            daily_switch.isChecked = true
        }

        daily_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref.setDailyState(true)
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("Notif","Daily")
                val pendingIntent = PendingIntent.getBroadcast(this, 100, intent, 0)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

            } else {
                sharedPref.setDailyState(false)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val reqCode = 100
                val pendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, 0)
                pendingIntent.cancel()
                alarmManager.cancel(pendingIntent)
            }
        }

        if (sharedPref.loadReleaseMovie() == true) {
            releaseMovie_switch.isChecked = true
        }

        releaseMovie_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref.setReleaseMovie(true)
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("Notif", "Release")
                val pendingIntent = PendingIntent.getBroadcast(this, 101, intent, 0)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

            } else {
                sharedPref.setReleaseMovie(false)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val reqCode = 101
                val pendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, 0)
                pendingIntent.cancel()
                alarmManager.cancel(pendingIntent)
            }
        }

    }

    private fun restartApp() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(applicationContext,
            R.anim.fade_in,
            R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
        intent.putExtra("Move","Favorite")
        startActivity(intent)
        super.onBackPressed()
        this.overridePendingTransition(
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
    }
}
