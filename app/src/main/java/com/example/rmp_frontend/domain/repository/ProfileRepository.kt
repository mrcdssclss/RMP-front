package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.User

interface ProfileRepository {
    suspend fun getProfile(): User
}
