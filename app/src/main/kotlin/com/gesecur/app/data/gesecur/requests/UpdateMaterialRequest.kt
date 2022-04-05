package com.gesecur.app.data.gesecur.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMaterialRequest(
        @SerialName("ot_id") val workOrderId: Long,
        @SerialName("producto_id") val materialId: Long,
        @SerialName("cantidad") val quantity: Int
) {
}