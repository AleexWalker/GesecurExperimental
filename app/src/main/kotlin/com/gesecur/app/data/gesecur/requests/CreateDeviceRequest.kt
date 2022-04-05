package com.gesecur.app.data.gesecur.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateDeviceRequest(
    @SerialName("registration_id") val token: String,
    @SerialName("cloud_message_type") val cloudMessageType: String = "FCM",
    @SerialName("application_id") val applicationId: String = "gesecur"
)