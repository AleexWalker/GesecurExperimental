package com.gesecur.app.domain.models

import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class Turn(
    @SerialName("turno_id") val id: Long,
    @SerialName("estado_id") val status: STATE,
    @SerialName("estado_descripcion") val statusDesc: String,
    @SerialName("vigilante_nombre") val vigilantName: String,
    @SerialName("lat_ini")  val latIni: Double?,
    @SerialName("lon_ini") val lonIni: Double?,
    @SerialName("lat_fin") val latEnd: Double?,
    @SerialName("lon_fin") val lonEnd: Double?,
    @SerialName("fecha_inicio")
    @Serializable(with = LocalDateTimeSerializer::class)
    val initDate: LocalDateTime?,
    @SerialName("fecha_fin")
    @Serializable(with = LocalDateTimeSerializer::class)
    val finishDate: LocalDateTime?
) {

    constructor(id: Long, statusId: STATE, initDate: LocalDateTime): this(id, statusId, "", "", null, null, null, null, initDate, null)

    @Serializable
    enum class STATE(val id: Int) {
        @SerialName("0")
        READY(0),

        @SerialName("1")
        ACTIVE(1),

        @SerialName("2")
        FINISHED(2)
    }
}