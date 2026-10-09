package com.codeandpray.luckygame.user.dto

import java.time.Instant

/**
 * Ответ с данными пользователя для клиента.
 */
data class UserResponse(
    val id: Long,
    val username: String,
    val points: Long,
    val createdAt: Instant
)