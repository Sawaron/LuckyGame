package com.codeandpray.luckygame.game.guessnumber.dto

import java.math.BigDecimal

data class GuessNumberDetailsResponse (
    val guessedNumber: Int,
    val drawnNumber: Int,
    val minNumber: Int,
    val maxNumber: Int,
    val payoutMultiplier: BigDecimal
)