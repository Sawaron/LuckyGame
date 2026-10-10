package com.codeandpray.luckygame.game.guessnumber.controller

import com.codeandpray.luckygame.game.guessnumber.dto.GuessNumberRulesResponse
import com.codeandpray.luckygame.game.guessnumber.dto.PlayGuessNumberRequest
import com.codeandpray.luckygame.game.guessnumber.dto.PlayGuessNumberResponse
import com.codeandpray.luckygame.game.guessnumber.service.GuessNumberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Контроллер игры «угадай число»: передаёт запросы в GuessNumberService
@RestController
@RequestMapping("/games/guess-number")
class GuessNumberController(private val guessNumberService: GuessNumberService) {

    // GET /games/guess-number/rules: возвращает серверные правила игры и шанс выигрыша
    @GetMapping("/rules")
    fun getRules(): GuessNumberRulesResponse =
        guessNumberService.getRules()

    // POST /games/guess-number/play: играет один раунд и возвращает результат с итоговым балансом
    @PostMapping("/play")
    fun play(@Valid @RequestBody request: PlayGuessNumberRequest): PlayGuessNumberResponse =
        guessNumberService.play(request)
}
