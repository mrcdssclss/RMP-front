package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.UserDto
import com.example.rmp_frontend.data.dto.UpdateProfileRequestDto
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.PATCH

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(): UserDto

    @PATCH("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto)
}
