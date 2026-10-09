package com.codeandpray.luckygame.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserResponse(
    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    @field:Size(max = 255, message = "Имя слишком длинное (макс. 255)")
    val username: String
)