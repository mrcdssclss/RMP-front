package com.example.rmp_frontend.data.dto

data class LoginRequestDto(
    val email: String,
    val password: String,
)

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
)

data class AuthResponseDto(
    val token: String,
)
