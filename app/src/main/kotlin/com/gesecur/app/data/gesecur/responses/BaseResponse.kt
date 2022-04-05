package com.gesecur.app.data.gesecur.responses

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: Int,
    val result: T
)