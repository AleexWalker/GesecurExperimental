package com.gesecur.app.ui.vigilant.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gesecur.app.R
import com.gesecur.app.domain.notifications.NotificationID
import com.gesecur.app.ui.vigilant.VigilantActivity

class NewsReminderScheduleWorker(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val ACTION_NEWS = "ACTION_NEWS"
    }

    override fun doWork(): Result {
        val channelId = context.getString(R.string.DEFAULT_NOTIFICATION_CHANNEL_ID)
        val intent = Intent(context, VigilantActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = ACTION_NEWS

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.APP_NAME))
            .setContentText(
                context.getString(R.string.NOTIFICATION_TURN_TEXT)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NotificationID.getID(), builder.build())
        }

        return Result.success()
    }

}