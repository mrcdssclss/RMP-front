package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.ProfileApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.User
import com.example.rmp_frontend.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
) : ProfileRepository {
    override suspend fun getProfile(): User {
        return profileApi.getProfile().toDomain()
    }
}
