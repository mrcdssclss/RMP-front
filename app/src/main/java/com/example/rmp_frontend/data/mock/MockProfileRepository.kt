package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.User
import com.example.rmp_frontend.domain.repository.ProfileRepository
import kotlinx.coroutines.delay

class MockProfileRepository : ProfileRepository {
    override suspend fun getProfile(): User {
        delay(200)
        return MockData.user
    }
}
