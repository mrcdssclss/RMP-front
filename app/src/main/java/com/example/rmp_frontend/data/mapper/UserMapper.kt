package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.UserDto
import com.example.rmp_frontend.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        login = login,
        displayName = displayName ?: login,
        email = email,
    )
}
