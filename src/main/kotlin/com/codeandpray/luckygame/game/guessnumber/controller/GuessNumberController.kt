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

@RestController
@RequestMapping("/games/guess-number")
class GuessNumberController(private val guessNumberService: GuessNumberService) {

    @GetMapping("/rules")
    fun getRules(): GuessNumberRulesResponse =
        guessNumberService.getRules()

    @PostMapping("/play")
    fun play(@Valid @RequestBody request: PlayGuessNumberRequest): PlayGuessNumberResponse =
        guessNumberService.play(request)
}
