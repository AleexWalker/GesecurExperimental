package com.gesecur.app.domain.notifications

import java.util.concurrent.atomic.AtomicInteger

object NotificationID {
    private val id = AtomicInteger(0)

    fun getID(): Int = id.incrementAndGet()

}