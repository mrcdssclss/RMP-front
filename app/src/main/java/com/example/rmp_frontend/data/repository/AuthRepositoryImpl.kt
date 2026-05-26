package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.AuthApi
import com.example.rmp_frontend.data.dto.LoginRequestDto
import com.example.rmp_frontend.data.dto.RegisterRequestDto
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.data.storage.TokenStorage
import com.example.rmp_frontend.domain.model.AuthToken
import com.example.rmp_frontend.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun login(login: String, password: String): AuthToken {
        val token = authApi.login(LoginRequestDto(login, password)).toDomain()
        tokenStorage.saveToken(token)
        return token
    }

    override suspend fun register(login: String, password: String, firstName: String?): AuthToken {
        val token = authApi.register(RegisterRequestDto(login, password, firstName)).toDomain()
        tokenStorage.saveToken(token)
        return token
    }

    override suspend fun currentToken(): AuthToken? = tokenStorage.getToken()

    override suspend fun logout() {
        tokenStorage.clearToken()
    }
}
