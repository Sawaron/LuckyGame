package com.codeandpray.luckygame.user.service

import com.codeandpray.luckygame.points.service.PointService
import com.codeandpray.luckygame.user.dto.UserRequest
import com.codeandpray.luckygame.user.dto.UserResponse
import com.codeandpray.luckygame.user.entity.User
import com.codeandpray.luckygame.user.exception.UserNotFoundException
import com.codeandpray.luckygame.user.exception.UsernameAlreadyExistsException
import com.codeandpray.luckygame.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val pointService: PointService,
    @Value("\${game.start-points:1000}") private val initialPoints: Long
) {
    @Transactional
    fun create(request: UserRequest): UserResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw UsernameAlreadyExistsException(request.username)
        }
        val user = User(request.username)

        userRepository.save(user)

        pointService.grantInitialPoints(user, initialPoints)

        return toResponse(user)
    }
    fun getById(userId:Long): UserResponse {
        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException(userId)
        }
        return toResponse(user)
    }

    private fun toResponse(user: User): UserResponse {
        val userResponse = UserResponse(
            id = requireNotNull(user.id) { "Id пользователя ${user.username} пуст" },
            username = user.username,
            points = user.points,
            createdAt = user.createdAt
        )
        return userResponse
    }
}