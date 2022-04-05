package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializerNoNanoSec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Personal(
    @SerialName("parte_personal_id") val partPersonalId: Long = -1,
    @SerialName("personal_id") val personalId: Long = -1,
    @SerialName("personal_nombre") val name: String? = "",
    @SerialName("personal_nombre_completo") val completeName: String? = "",
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    @SerialName("fecha_inicio") val initDate: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    @SerialName("fecha_fin") val endDate: LocalDateTime?
) {

    fun hasFinised() = initDate != null && endDate != null

    fun getPersonalName() = if(completeName?.isBlank() == true) name else completeName
}