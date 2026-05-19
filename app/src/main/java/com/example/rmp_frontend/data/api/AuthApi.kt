package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.AuthRequestDto
import com.example.rmp_frontend.data.dto.AuthResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequestDto): AuthResponseDto
}
