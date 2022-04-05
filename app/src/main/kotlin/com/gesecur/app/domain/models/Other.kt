package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Other(
        @SerialName("parte_otros_linea_id") val otherPartId: Long? = -1,
        @SerialName("parte_id") val partId: Long? = -1,
        @SerialName("ot_otros_linea_id") val otherLineId: Long? = -1,
        @SerialName("ot_id") val otherOtId: Long? = -1,
        @SerialName("otros_descripcion") val description: String,
        @SerialName("cantidad") var quantity: Int,
        @SerialName("cantidad_no_disponible") val notAvailableQty: Int = 0,
        @SerialName("cantidad_ot") val otQuantity: Int = 0,
)