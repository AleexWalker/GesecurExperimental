package com.gesecur.app.domain.models

import android.os.Parcelable
import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializer
import com.gesecur.app.data.gesecur.serializers.LocalDateTimeSerializerNoNanoSec
import com.gesecur.app.utils.formatRender
import com.gesecur.app.utils.toHour
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
@Parcelize
data class WorkPlanification(
    @SerialName("planificacion_id") val planiId: Long?,
    @SerialName("planificacion_descripcion") val desc: String ? = "",
    @SerialName("ot_id") val otId: Long? = -1L,
    @SerialName("ubicacion_direccion") val address: String? = null,
    @SerialName("planificacion_estado_id") val state: STATE? = STATE.PLANNED,
    @SerialName("planificacion_estado_descripcion") val stateDesc: String = "",
    @SerialName("planificacion_estado_color") val stateColor: String = "#000000",
    @SerialName("ubicacion_latitud") val latitude: Double,
    @SerialName("ubicacion_longitud") val longitude: Double,
    @SerialName("ubicacion_poblacion") val city: String? = null,
    @SerialName("ubicacion_provincia") val province: String? = null,
    @SerialName("ubicacion_datos_via") val streeData: String,
    @SerialName("ubicacion_nombre_via") val streetName: String,
    @SerialName("ubicacion_codigo_postal") val postalCode: String,
    @SerialName("cliente_id") val clientId: Long? = null,
    @SerialName("cliente_nombre") val clientName: String = "",
    @SerialName("partes") val parts: List<WorkPart>? = null,
    @SerialName("materiales") val materials: @RawValue List<Material> = arrayListOf(),
    @SerialName("otros") val others: @RawValue List<Other> = arrayListOf(),
    @SerialName("trabajos") val jobs: @RawValue List<Job> = arrayListOf(),
    @SerialName("servicios") val services: @RawValue List<Service> = arrayListOf(),
    @SerialName("personal") val personal: @RawValue List<Personal> = arrayListOf(),
    @SerialName("fecha_inicio")
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    val dateIni: LocalDateTime?,
    @SerialName("fecha_fin")
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    val dateEnd: LocalDateTime?

        ) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkPlanification) return false

        if (planiId != other.planiId) return false

        return true
    }

    override fun hashCode(): Int {
        return planiId?.hashCode() ?: -1
    }

    fun hasStartedPlanificationForDate(date: LocalDate): Boolean {
        return getPartByDate(date) != null
    }

    fun getPartByDate(date: LocalDate) = parts?.find { it.dateIni?.toLocalDate() == date }

    fun isPastPart() = dateIni?.toLocalDate()?.isBefore(LocalDate.now()) == true

    fun getCompleteAddress() = address ?: "$streetName $streeData, $postalCode, $city, $province"

    fun getDateWithHours() = "${dateIni?.toLocalDate()?.formatRender() ?: ""} ${dateIni?.toHour() ?: ""} - ${dateEnd?.toHour() ?: ""}"


    @Serializable
    enum class STATE(val id: Int) {
        @SerialName("4")
        PLANNED(4),

        @SerialName("1")
        STARTED(1),

        @SerialName("2")
        FINISHED(2),

        @SerialName("3")
        NOT_FINISHED(3)
    }
}