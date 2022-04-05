package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncidenceVigilantType(
    @SerialName("tipo_id") val id: Long,
    @SerialName("descripcion") val description: String)
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IncidenceVigilantType) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}