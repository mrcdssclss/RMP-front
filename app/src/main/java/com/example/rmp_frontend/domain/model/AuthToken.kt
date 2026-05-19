package com.example.rmp_frontend.domain.model

data class AuthToken(
    val accessToken: String,
    val tokenType: String = "Bearer",
)
