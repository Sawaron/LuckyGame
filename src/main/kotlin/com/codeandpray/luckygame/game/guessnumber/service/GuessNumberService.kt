package com.codeandpray.luckygame.game.guessnumber.service

import com.codeandpray.luckygame.game.entity.GameRound
import com.codeandpray.luckygame.game.entity.GameType
import com.codeandpray.luckygame.game.entity.RoundOutcome
import com.codeandpray.luckygame.game.exception.InvalidGameRequestException
import com.codeandpray.luckygame.game.guessnumber.dto.GuessNumberRulesResponse
import com.codeandpray.luckygame.game.guessnumber.dto.PlayGuessNumberRequest
import com.codeandpray.luckygame.game.guessnumber.dto.PlayGuessNumberResponse
import com.codeandpray.luckygame.game.guessnumber.entity.GuessNumberDetails
import com.codeandpray.luckygame.game.guessnumber.math.GuessNumberEngine
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberInput
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberRules
import com.codeandpray.luckygame.game.repository.GameRoundRepository
import com.codeandpray.luckygame.points.exception.InsufficientPointsException
import com.codeandpray.luckygame.points.service.PointService
import com.codeandpray.luckygame.user.exception.UserNotFoundException
import com.codeandpray.luckygame.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GuessNumberService(
    private val userRepository: UserRepository,
    private val gameRoundRepository: GameRoundRepository,
    private val pointService: PointService,
    private val guessNumberEngine: GuessNumberEngine,
    private val guessNumberRules: GuessNumberRules
) {

    fun getRules(): GuessNumberRulesResponse {
        val odds = guessNumberEngine.calculateOdds(guessNumberRules, guessNumberRules.minBet)

        return GuessNumberRulesResponse(
            minNumber = guessNumberRules.minNumber,
            maxNumber = guessNumberRules.maxNumber,
            minBet = guessNumberRules.minBet,
            maxBet = guessNumberRules.maxBet,
            winProbability = odds.winProbability,
            payoutMultiplier = guessNumberRules.payoutMultiplier
        )
    }

    @Transactional
    fun play(request: PlayGuessNumberRequest): PlayGuessNumberResponse {
        if (request.guessedNumber !in guessNumberRules.minNumber..guessNumberRules.maxNumber) {
            throw InvalidGameRequestException(
                "Выбранное число должно быть от ${guessNumberRules.minNumber} до ${guessNumberRules.maxNumber}"
            )
        }
        if (request.bet !in guessNumberRules.minBet..guessNumberRules.maxBet) {
            throw InvalidGameRequestException(
                "Ставка должна быть от ${guessNumberRules.minBet} до ${guessNumberRules.maxBet} поинтов"
            )
        }

        val user = userRepository.findByIdForUpdate(request.userId)
            ?: throw UserNotFoundException(request.userId)

        if (user.points < request.bet) {
            throw InsufficientPointsException(user.points, request.bet)
        }

        val input = GuessNumberInput(guessedNumber = request.guessedNumber, bet = request.bet)
        val result = guessNumberEngine.play(input, guessNumberRules)

        val outcome = if (result.won) RoundOutcome.WIN else RoundOutcome.LOSS

        val details = GuessNumberDetails(
            guessedNumber = result.guessedNumber,
            drawnNumber = result.drawnNumber,
            minNumber = guessNumberRules.minNumber,
            maxNumber = guessNumberRules.maxNumber,
            payoutMultiplier = guessNumberRules.payoutMultiplier
        )

        val gameRound = GameRound(
            userId = requireNotNull(user.id) { "User ID is null" },
            gameType = GameType.GUESS_NUMBER,
            bet = result.bet,
            payout = result.payout,
            outcome = outcome,
            guessNumberDetails = details
        )

        val savedRound = gameRoundRepository.save(gameRound)
        val roundId = requireNotNull(savedRound.id) { "Saved round ID is null" }

        pointService.debitBet(user, roundId, result.bet)

        if (result.payout > 0) {
            pointService.creditPayout(user, roundId, result.payout)
        }

        return PlayGuessNumberResponse(
            roundId = roundId,
            guessedNumber = result.guessedNumber,
            drawnNumber = result.drawnNumber,
            outcome = outcome,
            bet = result.bet,
            payout = result.payout,
            points = user.points
        )
    }
}