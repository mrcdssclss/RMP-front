package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.UserDto
import retrofit2.http.GET

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(): UserDto
}
