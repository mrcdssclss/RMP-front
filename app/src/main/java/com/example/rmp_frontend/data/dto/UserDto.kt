package com.example.rmp_frontend.data.dto

data class UserDto(
    val userId: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val balanceCurrency: String,
    val balanceAmount: Double,
)

data class UpdateProfileRequestDto(
    val firstName: String? = null,
    val lastName: String? = null,
)
