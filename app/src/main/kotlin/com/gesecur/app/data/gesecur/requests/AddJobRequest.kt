package com.gesecur.app.data.gesecur.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Field

@Serializable
class AddJobRequest(
        @SerialName("ot_id") val workOrderId: Long,
        @SerialName("trabajo_id") val jobId: Long = -1,
        @SerialName("parte_id") val partId: Long = -1,
        @SerialName("trabajo_codificado") val isCodified: Int
) {
}