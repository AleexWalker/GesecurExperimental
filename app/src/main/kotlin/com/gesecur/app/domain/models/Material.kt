package com.gesecur.app.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Material(
    @SerialName("producto_id") val id: Long,
    @SerialName("parte_material_id") val parteMaterialId: Long = -1,
    @SerialName("producto_concepto") val concept: String? = null,
    @SerialName("producto_descripcion") val description: String? = null,
    @SerialName("producto_nombre") val name: String? = null,
    @SerialName("planificacion_cantidad") val planiQuantity: Int? = 0,
    @SerialName("planificacion_id") val planiId: Long,
    @SerialName("precio_ud_venta") val manualPrice:  Double = 0.toDouble(),
    @SerialName("cantidad") var quantity: Int = 0,
    @SerialName("cantidad_no_disponible") val notAvailableQty: Int = 0,
    @SerialName("cantidad_ot") val otQuantity: Int = 0,
    @SerialName("producto_codigo") val code: String,
    @SerialName("extra") var extra: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Material) return false

        if (id != other.id) return false

        return true
    }

    fun mapToProduct() = Product(
        id, parteMaterialId, concept, "", planiId, manualPrice, 0.toDouble(),0.toDouble(),quantity, code, extra)

    override fun hashCode(): Int {
        return id.hashCode()
    }
}