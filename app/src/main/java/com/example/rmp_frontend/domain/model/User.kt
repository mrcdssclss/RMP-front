package com.example.rmp_frontend.domain.model

data class User(
    val id: String,
    val login: String,
    val displayName: String,
    val email: String? = null,
)
