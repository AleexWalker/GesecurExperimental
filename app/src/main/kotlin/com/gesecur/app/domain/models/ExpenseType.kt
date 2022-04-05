package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseType(
    @SerialName("tipo_gasto_id") val id: Long,
    @SerialName("tipo_gasto_descripcion") val description: String)
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExpenseType) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}