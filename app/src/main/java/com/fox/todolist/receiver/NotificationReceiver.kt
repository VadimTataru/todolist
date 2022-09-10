package com.fox.todolist.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.fox.todolist.R
import com.fox.todolist.utils.Constants.CHANNEL_ID
import com.fox.todolist.utils.Constants.GROUP_MESSAGE
import com.fox.todolist.utils.Constants.NOTE_DESC_EXTRA
import com.fox.todolist.utils.Constants.NOTE_TITLE_EXTRA
import com.fox.todolist.utils.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import com.fox.todolist.utils.Constants.NOTIFICATION_CHANNEL_NAME
import java.util.*

class NotificationReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = intent.getStringExtra(NOTE_TITLE_EXTRA)
        val description = intent.getStringExtra(NOTE_DESC_EXTRA)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.CYAN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setGroup(GROUP_MESSAGE)
            .build()


        notificationManager.notify(getNotificationId(), notification)
    }

    private fun getNotificationId(): Int = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
}