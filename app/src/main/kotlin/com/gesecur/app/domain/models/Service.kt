package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
        @SerialName("orden_trabajo_id") val otId: Long,
        @SerialName("servicio_id") val id: Long,
        @SerialName("servicio_tipo_codigo") val code: String,
        @SerialName("servicio_tipo_descripcion") val description: String,
        @SerialName("servicio_tipo_nombre") val name: String,
        @SerialName("servicio_tipo_id") val typeId: Long
)