package com.example.rmp_frontend.data.mapper

import com.example.rmp_frontend.data.dto.UserDto
import com.example.rmp_frontend.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = userId,
        login = email,
        displayName = listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { email },
        email = email,
    )
}
