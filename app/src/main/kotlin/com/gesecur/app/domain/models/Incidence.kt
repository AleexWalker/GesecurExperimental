package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Incidence(
        @SerialName("incidencia_id") val id: Long,
        @SerialName("incidencia_cod_id") val codeId: Long?,
        @SerialName("incidencia_cod_descripcion") val codeDesc: String?,
        @SerialName("ubicacion_direccion") val address: String,
        @SerialName("lat") val latitude: Double,
        @SerialName("lon") val longitude: Double,
        @SerialName("descripcion") val description: String,
        @SerialName("fecha")
        @Serializable(with = LocalDateTimeSerializer::class)
        val date: LocalDateTime?,
        @SerialName("file_src") val file: String?
        )
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Incidence) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}