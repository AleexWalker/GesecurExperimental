package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Vehicle(
    @SerialName("vehiculo_id") val id: Long,
    @SerialName("matricula") val plate: String,
    @SerialName("marca") val brand: String,
    @SerialName("modelo") val model: String,
) {

    override fun toString(): String {
        return "$model $plate"
    }
}