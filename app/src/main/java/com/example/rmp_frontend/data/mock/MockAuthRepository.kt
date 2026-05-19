package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.data.storage.TokenStorage
import com.example.rmp_frontend.domain.model.AuthToken
import com.example.rmp_frontend.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class MockAuthRepository(
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun login(login: String, password: String): AuthToken {
        delay(250)
        require(login.isNotBlank()) { "Введите логин" }
        require(password.isNotBlank()) { "Введите пароль" }
        return AuthToken(accessToken = "mock-token-$login").also { tokenStorage.saveToken(it) }
    }

    override suspend fun register(login: String, password: String): AuthToken {
        delay(300)
        require(login.isNotBlank()) { "Введите логин" }
        require(password.length >= 4) { "Пароль должен быть не короче 4 символов" }
        return AuthToken(accessToken = "mock-token-$login").also { tokenStorage.saveToken(it) }
    }

    override suspend fun currentToken(): AuthToken? = tokenStorage.getToken()

    override suspend fun logout() {
        tokenStorage.clearToken()
    }
}
