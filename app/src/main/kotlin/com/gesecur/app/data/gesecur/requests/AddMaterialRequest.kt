package com.gesecur.app.data.gesecur.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AddMaterialRequest(
        @SerialName("ot_id") val otId: Long,
        @SerialName("producto_id") val productId: Long,
        @SerialName("ot_servicio_material_id") val otServiceMaterialId: Long = -1L
) {
}