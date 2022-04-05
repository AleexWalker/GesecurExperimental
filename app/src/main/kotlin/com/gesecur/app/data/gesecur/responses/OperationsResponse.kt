package com.gesecur.app.data.gesecur.responses

import kotlinx.serialization.Serializable

@Serializable
data class OperationsResponse(
        val status: Int,
        val title: String,
        val message: String
) {
}