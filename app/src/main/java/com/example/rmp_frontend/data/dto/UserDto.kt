package com.example.rmp_frontend.data.dto

data class UserDto(
    val id: String,
    val login: String,
    val displayName: String? = null,
    val email: String? = null,
)
