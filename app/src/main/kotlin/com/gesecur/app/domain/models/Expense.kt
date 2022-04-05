package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializerNoNanoSec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Expense(
        @SerialName("personal_gasto_id") val id: Long,
        @SerialName("parte_id") val partId: Long? = 0,
        @SerialName("estado_id") val statusId: Long? = -1,
        @SerialName("estado_descripcion") val statusDesc: String? ="",
        @SerialName("nombre_completo") val name: String? ="",
        @SerialName("estado_color") val statusColor: String? = "#fff",
        @SerialName("nota") val description: String,
        @SerialName("importe") val price: Double,
        @SerialName("cantidad") val quantity: Int,
        @SerialName("tipo_gasto_id") var typeId: Long?,
        @SerialName("tipo_gasto_descripcion") val typeDesc: String?,
        @SerialName("file_src") val file: String?,
        @SerialName("fecha")
        @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
        val date: LocalDateTime?
) {
}