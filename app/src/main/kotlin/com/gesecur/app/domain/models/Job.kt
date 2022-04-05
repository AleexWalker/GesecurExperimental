package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Job(
        @SerialName("trabajo_id") val id: Long,
        @SerialName("parte_trabajo_id") val workPartId: Long = -1,
        @SerialName("parte_id") val partId: Long? = -1,
        @SerialName("trabajo_descripcion") val description: String,
        @SerialName("cantidad") var quantity: Int,
        @SerialName("cantidad_no_disponible") val notAvailableQty: Int = 0,
        @SerialName("cantidad_ot") val otQuantity: Int = 0,
        @SerialName("checked") var checked: Boolean? = false,
        @SerialName("duracion") val duration: Int? = 0,
        @SerialName("extra") val extra: Boolean = false,
        @SerialName("codified") val codified: Boolean? = true,
        @SerialName("observaciones") val observations: String? = ""
) {

        fun extraAsInt() = if(extra) 1 else 0

        fun isCodified() = if(codified == true) 1 else 0

        fun checkedAsInt() = if(checked == true) 1 else 0
}