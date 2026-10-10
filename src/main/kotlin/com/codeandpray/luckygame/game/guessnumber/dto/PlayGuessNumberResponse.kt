package com.codeandpray.luckygame.game.guessnumber.dto

import com.codeandpray.luckygame.game.entity.RoundOutcome

data class PlayGuessNumberResponse (
    val roundId: Long,
    val guessedNumber: Int,
    val drawnNumber: Int,
    val outcome: RoundOutcome,
    val bet: Long,
    val payout: Long,
    val points: Long
)