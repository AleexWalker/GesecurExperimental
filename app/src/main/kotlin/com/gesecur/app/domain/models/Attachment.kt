package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializerNoNanoSec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Attachment(
        @SerialName("parte_adjunto_id") val id: Long? = -1,
        @SerialName("parte_id") val partId: Long? = -1,
        @SerialName("personal_nombre_completo") val personalName: String? = "",
        @SerialName("fecha")
        @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
        val date: LocalDateTime?,
        @SerialName("ruta_imagen") var image: String? = null,
        @SerialName("notas") val notes: String = "",
        @SerialName("adjunto_tipo_descripcion") val desc: String? = "",
)