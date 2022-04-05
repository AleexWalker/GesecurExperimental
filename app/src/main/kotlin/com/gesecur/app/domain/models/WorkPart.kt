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
data class WorkPart(
    @SerialName("parte_id") val id: Long?,
    @SerialName("planificacion_id") val planiId: Long?,
    @SerialName("planificacion_descripcion") val desc: String ? = "",
    @SerialName("orden_trabajo_id") val otId: Long? = -1L,
    @SerialName("ubicacion_direccion") val address: String,
    @SerialName("estado_id") val state: STATE? = STATE.STARTED,
    @SerialName("estado_color") val stateColor: String = "#000000",
    @SerialName("ubicacion_latitud") val latitude: Double,
    @SerialName("ubicacion_longitud") val longitude: Double,
    @SerialName("ubicacion_codigo_postal") val postalCode: String,
    @SerialName("materiales") val materials: @RawValue List<Material> = arrayListOf(),
    @SerialName("adjuntos") val attachments: @RawValue List<Attachment> = arrayListOf(),
    @SerialName("otros") val others: @RawValue List<Other> = arrayListOf(),
    @SerialName("trabajos") val jobs: @RawValue List<Job> = arrayListOf(),
    @SerialName("servicios") val services: @RawValue List<Service> = arrayListOf(),
    @SerialName("personal") val personal: @RawValue List<Personal> = arrayListOf(),
    @SerialName("conformidad_cliente") val clientConfirmation: Int? = 0,
    @SerialName("conformidad_realizada") val confirmationDone: Boolean = false,
    @SerialName("fecha_inicio")
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    val dateIni: LocalDateTime?,
    @SerialName("fecha_fin")
    @Serializable(with = LocalDateTimeSerializerNoNanoSec::class)
    val dateEnd: LocalDateTime?

        ) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkPart) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: -1
    }

    fun hasFinishedPart() = state == STATE.FINISHED || dateEnd != null  // and clientConfirmation == 0 ????

    fun isPastPart() = dateIni?.toLocalDate()?.isBefore(LocalDate.now()) == true

    fun getDateWithHours() = "${dateIni?.toLocalDate()?.formatRender() ?: ""} ${dateIni?.toHour() ?: ""} - ${dateEnd?.toHour() ?: ""}"

    @Serializable
    enum class STATE(val id: Int) {
        @SerialName("1")
        STARTED(1),

        @SerialName("2")
        FINISHED(2),

        @SerialName("3")
        FINISHED_WITH_FAILS(3),

        @SerialName("4")
        ESCALADO(4)
    }
}