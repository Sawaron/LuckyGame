package com.codeandpray.luckygame.user.controller

import com.codeandpray.luckygame.user.dto.UserRequest
import com.codeandpray.luckygame.user.dto.UserResponse
import com.codeandpray.luckygame.user.service.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Validated
class UserController(private val userService: UserService) {

    @PostMapping
    fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        val response = userService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{userId}")
    fun getById(@PathVariable @Positive(message = "ID пользователя должен быть положительным") userId: Long): UserResponse =
        userService.getById(userId)
}
