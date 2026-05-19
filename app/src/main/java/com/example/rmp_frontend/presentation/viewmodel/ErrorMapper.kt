package com.example.rmp_frontend.presentation.viewmodel

internal fun Throwable.toUserMessage(): String {
    return message?.takeIf { it.isNotBlank() } ?: "Не удалось выполнить запрос"
}
