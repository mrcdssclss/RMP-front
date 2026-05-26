package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.AuthResponseDto
import com.example.rmp_frontend.domain.model.AuthToken

fun AuthResponseDto.toDomain(): AuthToken {
    return AuthToken(
        accessToken = token,
        tokenType = "Bearer",
    )
}
