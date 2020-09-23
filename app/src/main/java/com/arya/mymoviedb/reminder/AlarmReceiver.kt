package com.arya.mymoviedb.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arya.mymoviedb.BuildConfig
import com.arya.mymoviedb.R
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.movie.MovieFragment
import com.arya.mymoviedb.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("@RECEIVE", "Notifications Succes")
        val status = intent.getStringExtra("Notif")
        Log.d("@RECEIVE", "Notifications $status")
        if (status == "Daily") {
            dailyNotification(context)
        } else {
            releaseMovie(context)
        }
    }

    private fun dailyNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, "daily_notify")
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle("Daily Reminder")
            .setContentText("Check some movies or tv shows")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(path)

        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "daily_channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("daily_notify", name, importance)

            val notificationM = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationM.createNotificationChannel(channel)
        }

        notificationManager.notify(1, builder.build())
    }

    private fun releaseNotification(context: Context, title: String, desc: String, i: Int) {
        val builder = NotificationCompat.Builder(context, "release_notify")
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(path)

        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "release_channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("release_notify", name, importance)

            val notificationM = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationM.createNotificationChannel(channel)
        }

        notificationManager.notify(i, builder.build())
    }

    private fun releaseMovie(context: Context) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val now = dateFormat.format(date)
        Log.d("@TIME", now)

        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.releaseMovie(BuildConfig.API_KEY, now, now).enqueue(object:
            Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(MovieFragment::class.java.simpleName, "${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResult = response.body()?.result
                    var id = 2
                    for (i in movieResult!!.indices) {
                        val movie = movieResult[i]
                        Log.d("@RECEIVE", "$movie")
                        val title = movie.title
                        Log.d("@RECEIVE", title)
                        val desc = "$title has been released today"
                        releaseNotification(context, title, desc, i)
                        id++
                    }
                }
            }

        })
    }

}