package com.example.rmp_frontend.data.dto

data class AuthRequestDto(
    val login: String,
    val password: String,
)

data class AuthResponseDto(
    val accessToken: String,
    val tokenType: String? = null,
)
