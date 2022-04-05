package com.gesecur.app.data.gesecur.responses

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponseNullable<T>(
    val status: Int,
    val result: T? = null,
    val message: String? = null
)