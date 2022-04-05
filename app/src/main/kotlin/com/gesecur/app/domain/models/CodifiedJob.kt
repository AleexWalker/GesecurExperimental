package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodifiedJob(
        @SerialName("trabajo_cod_id") val id: Long,
        @SerialName("descripcion") val description: String,
) {

    fun isReallyCodified() = if(id > -1) 1 else 0
}