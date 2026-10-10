package com.codeandpray.luckygame.game.controller

import com.codeandpray.luckygame.common.dto.PageResponse
import com.codeandpray.luckygame.game.dto.GameRoundResponse
import com.codeandpray.luckygame.game.service.GameHistoryService
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/{userId}/rounds")
@Validated
class GameHistoryController(private val gameHistoryService: GameHistoryService) {


    @GetMapping
    fun getHistory(
        @PathVariable @Positive(message = "ID пользователя должен быть положительным") userId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): PageResponse<GameRoundResponse> =
        gameHistoryService.getHistory(userId, pageable)
}
