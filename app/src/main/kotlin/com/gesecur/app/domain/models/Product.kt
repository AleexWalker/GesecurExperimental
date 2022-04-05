package com.gesecur.app.domain.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Product(
        @SerialName("producto_id") val id: Long,
        @SerialName("parte_material_id") var parteMaterialId: Long = -1,
        @SerialName("descripcion") val description: String?,
        @SerialName("nombre") val name: String,
        @SerialName("parte_id") val partId: Long = -1,
        @SerialName("precio_manual") val manualPrice: Double = 0.toDouble(),
        @SerialName("precio_venta") val salePrice: Double = 0.toDouble(),
        @SerialName("precio_coste") val costPrice: Double = 0.toDouble(),
        @SerialName("cantidad") var quantity: Int = 0,
        @SerialName("codigo") val code: String,
        @SerialName("extra") var extra: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun extraAsInt() = if(extra) 1 else 0
}