package com.arya.mymoviedb.database

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private var sharedPref: SharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE)

    fun setNightModeState(state: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("NightMode", state)
        editor.apply()
    }

    fun loadNightModeState(): Boolean? {
        return sharedPref.getBoolean("NightMode", false)
    }

    fun setDailyState(state: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("DailyNotif", state)
        editor.apply()
    }

    fun loadDailyState(): Boolean? {
        return sharedPref.getBoolean("DailyNotif", false)
    }

    fun setReleaseMovie(state: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("ReleaseMovieNotif", state)
        editor.apply()
    }

    fun loadReleaseMovie(): Boolean? {
        return sharedPref.getBoolean("ReleaseMovieNotif", false)
    }

}