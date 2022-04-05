package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializerNoNanoSec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Mileage(
        @SerialName("personal_kilometraje_id") val id: Long,
        @SerialName("parte_id") val partId: Long? = null,
        @SerialName("estado_id") val statusId: Long? = -1,
        @SerialName("estado_descripcion") val statusDesc: String? ="",
        @SerialName("nombre_completo") val name: String? ="",
        @SerialName("estado_color") val statusColor: String? = "#fff",
        @SerialName("nota") val description: String,
        @SerialName("kilometraje_inicial") var kmIni: Double? = 0.toDouble(),
        @SerialName("kilometraje_final") val kmFin: Double?,
        @SerialName("file_src") val file: String,
        @SerialName("fecha")
        @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
        val date: LocalDateTime?
) {
}