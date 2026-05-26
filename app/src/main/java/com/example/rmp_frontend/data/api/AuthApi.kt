package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.LoginRequestDto
import com.example.rmp_frontend.data.dto.RegisterRequestDto
import com.example.rmp_frontend.data.dto.AuthResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto
}
