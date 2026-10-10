package com.codeandpray.luckygame.game.dto

import com.codeandpray.luckygame.game.entity.GameType
import com.codeandpray.luckygame.game.entity.RoundOutcome
import com.codeandpray.luckygame.game.guessnumber.dto.GuessNumberDetailsResponse
import java.time.Instant

data class GameRoundResponse (
    val id: Long,
    val gameType: GameType,
    val bet: Long,
    val payout: Long,
    val outcome: RoundOutcome,
    val guessNumberDetails: GuessNumberDetailsResponse?,
    val createdAt: Instant
)