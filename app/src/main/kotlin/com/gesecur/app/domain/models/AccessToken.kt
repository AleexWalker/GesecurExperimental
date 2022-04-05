package com.gesecur.app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
        val accessToken: String,
        val refreshToken: String
)