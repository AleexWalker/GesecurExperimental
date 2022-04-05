package com.gesecur.app.domain.initializers

import android.content.Context
import androidx.startup.Initializer
import com.gesecur.app.domain.notifications.NotificationsManager

class FCMInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        NotificationsManager.createNotificationChannel(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            TimberInitializer::class.java,
            KoinInitializer::class.java
        )
    }
}