package com.codeandpray.luckygame.points.controller

import com.codeandpray.luckygame.common.dto.PageResponse
import com.codeandpray.luckygame.points.service.PointService
import com.codeandpray.luckygame.user.dto.PointTransactionResponse
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Контроллер истории операций с поинтами: передаёт запросы в PointService
@RestController
@RequestMapping("/users/{userId}/point-transactions")
@Validated
class PointTransactionController(private val pointService: PointService) {

    // GET /users/{userId}/point-transactions: страница операций пользователя, новые сверху.
    // Если параметры страницы не переданы, берётся страница 0 размером 20
    @GetMapping
    fun getHistory(
        @PathVariable @Positive(message = "ID пользователя должен быть положительным") userId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): PageResponse<PointTransactionResponse> =
        pointService.getHistory(userId, pageable)
}
