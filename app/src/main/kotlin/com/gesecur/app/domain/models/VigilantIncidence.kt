package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Serializable
data class VigilantIncidence(
        @SerialName("incidencia_id") val id: Long,
        @SerialName("incidencia_tipo_id") val codeId: Int,
        @SerialName("incidencia_tipo_descripcion") val codeDesc: String,
        @SerialName("lat") val latitude: Double,
        @SerialName("lon") val longitude: Double,
        @SerialName("localizacion") val address: String,
        @SerialName("descripcion") val description: String,
        @SerialName("file_src")
        val file: String? = null,
        @SerialName("fecha")
        @Serializable(with = LocalDateTimeSerializer::class)
        val date: LocalDateTime? = null
        )
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VigilantIncidence) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}