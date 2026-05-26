package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.AuthToken

interface AuthRepository {
    suspend fun login(login: String, password: String): AuthToken
    suspend fun register(login: String, password: String, firstName: String? = null): AuthToken
    suspend fun currentToken(): AuthToken?
    suspend fun logout()
}
