package com.codeandpray.luckygame.game.guessnumber.dto

data class PlayGuessNumberRequest (
    val userId: Long,
    val guessedNumber: Int,
    val bet: Long
)