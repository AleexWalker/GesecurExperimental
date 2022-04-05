package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id") val id: Long,
    @SerialName("personal_id") val personalId: Long = -1,
    @SerialName("nombre") val firstName: String,
    @SerialName("apellidos") val lastName: String,
    @SerialName("mail") val email: String,
    @SerialName("role_id") var role: ROLE
) {

    fun isOperator() = role == ROLE.OPERATOR

    fun isVigilant() = role == ROLE.VIGILANT

    @Serializable
    enum class ROLE(val id: String) {
        @SerialName("1")
        OPERATOR("1"),

        @SerialName("2")
        VIGILANT("2")
    }
}