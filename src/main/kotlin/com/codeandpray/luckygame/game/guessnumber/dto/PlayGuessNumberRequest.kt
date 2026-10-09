package com.codeandpray.luckygame.game.guessnumber.dto

import jakarta.validation.constraints.Positive

data class PlayGuessNumberRequest (
    @field:Positive(message = "ID пользователя должен быть положительным")
    val userId: Long,

    // Для guessedNumber проверка диапазона (1..10) делается в СЕРВИСЕ,
    // так как диапазон берется из настроек (GuessNumberRules), а не захардкожен.
    val guessedNumber: Int,

    @field:Positive(message = "Ставка должна быть больше нуля")
    val bet: Long
)