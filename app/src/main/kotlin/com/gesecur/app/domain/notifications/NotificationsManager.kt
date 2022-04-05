package com.gesecur.app.domain.notifications

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.gesecur.app.R
import com.gesecur.app.ui.splash.SplashActivity

object NotificationsManager {

    fun handleNotification(context: Context, remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val channelId = context.getString(R.string.default_notification_channel_id)
            val title = if (it.title.isNullOrEmpty()) context.getString(R.string.APP_NAME) else it.title
            val message = it.body
//            val color = ContextCompat.getColor(context, R.color.)

            val notification = NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
//                .setColor(color)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(NotificationID.getID(), notification)
        }
    }


    fun createNotificationChannel(context: Context) {
        val id = context.getString(R.string.default_notification_channel_id)
        val name = context.getString(R.string.default_notification_channel_id)
        val description = context.getString(R.string.default_notification_channel_id)
        val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT

        val channel = NotificationChannelCompat.Builder(id, importance)
            .setName(name)
            .setDescription(description)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}