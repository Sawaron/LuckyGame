package com.codeandpray.luckygame.game.guessnumber.dto

import java.math.BigDecimal

data class GuessNumberRulesResponse (
    val minNumber: Int,
    val maxNumber: Int,
    val minBet: Long,
    val maxBet: Long,
    val winProbability: BigDecimal,
    val payoutMultiplier: BigDecimal
)