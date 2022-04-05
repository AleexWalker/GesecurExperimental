package com.gesecur.app.data.gesecur.responses

import com.gesecur.app.domain.models.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(
    @SerialName("usuario") val user: UserResponse,
    @SerialName("token") val token: String
) {

    @Serializable
    data class UserResponse(@SerialName("id") val id: Long)
}