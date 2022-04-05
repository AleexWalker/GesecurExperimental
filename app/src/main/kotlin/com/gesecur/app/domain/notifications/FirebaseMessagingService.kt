package com.gesecur.app.domain.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    //private val createDeviceUseCase: CreateDeviceUseCase by inject()

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

//        GlobalScope.launch {
//            createDeviceUseCase(newToken)
//        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        NotificationsManager.handleNotification(this, remoteMessage)
    }
}