package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.AuthApi
import com.example.rmp_frontend.data.dto.AuthRequestDto
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.data.storage.TokenStorage
import com.example.rmp_frontend.domain.model.AuthToken
import com.example.rmp_frontend.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun login(login: String, password: String): AuthToken {
        val token = authApi.login(AuthRequestDto(login, password)).toDomain()
        tokenStorage.saveToken(token)
        return token
    }

    override suspend fun register(login: String, password: String): AuthToken {
        val token = authApi.register(AuthRequestDto(login, password)).toDomain()
        tokenStorage.saveToken(token)
        return token
    }

    override suspend fun currentToken(): AuthToken? = tokenStorage.getToken()

    override suspend fun logout() {
        tokenStorage.clearToken()
    }
}
