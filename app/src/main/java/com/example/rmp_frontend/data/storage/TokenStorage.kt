package com.example.rmp_frontend.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.rmp_frontend.domain.model.AuthToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth")

class TokenStorage(private val context: Context) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val tokenTypeKey = stringPreferencesKey("token_type")

    suspend fun saveToken(token: AuthToken) {
        context.authDataStore.edit { preferences ->
            preferences[accessTokenKey] = token.accessToken
            preferences[tokenTypeKey] = token.tokenType
        }
    }

    suspend fun getToken(): AuthToken? {
        return context.authDataStore.data
            .map { preferences ->
                val accessToken = preferences[accessTokenKey]
                if (accessToken.isNullOrBlank()) {
                    null
                } else {
                    AuthToken(
                        accessToken = accessToken,
                        tokenType = preferences[tokenTypeKey] ?: "Bearer",
                    )
                }
            }
            .first()
    }

    suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(tokenTypeKey)
        }
    }
}
