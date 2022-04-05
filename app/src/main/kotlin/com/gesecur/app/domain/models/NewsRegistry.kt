package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class NewsRegistry(
    @SerialName("id") val id: Long,
    @SerialName("turno_id") val turnId: Long,
    @SerialName("registro_tipo_id") val type: TYPE? = TYPE.NO_NEWS,
    @SerialName("registro_tipo_descripcion") val statusDesc: String? = "",
    @SerialName("descripcion") val description: String?,
    @SerialName("fecha")
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime?
) {



    @Serializable
    enum class TYPE(val id: Int) {
        @SerialName("3")
        NO_NEWS(3),

        @SerialName("2")
        REQUIREMENT_NON_URGENT(2),

        @SerialName("1")
        URGENT(1)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NewsRegistry) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}