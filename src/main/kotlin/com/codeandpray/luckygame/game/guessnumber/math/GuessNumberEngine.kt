package com.codeandpray.luckygame.game.guessnumber.math

import com.codeandpray.luckygame.common.math.PayoutCalculator
import com.codeandpray.luckygame.common.math.ProbabilityCalculator
import com.codeandpray.luckygame.common.math.model.GameOdds
import com.codeandpray.luckygame.common.math.random.RandomNumberGenerator
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberInput
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberResult
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberRules

class GuessNumberEngine(
    private val probabilityCalculator: ProbabilityCalculator,
    private val payoutCalculator: PayoutCalculator,
    private val randomNumberGenerator: RandomNumberGenerator,
) {
    fun calculateOdds(rules: GuessNumberRules, bet: Long): GameOdds {
        validateBet(bet, rules)
        val totalOutcomes = rules.maxNumber.toLong() - rules.minNumber.toLong() + 1
        val probability = probabilityCalculator.calculate(1, totalOutcomes)
        val payout = payoutCalculator.calculatePayout(bet, rules.payoutMultiplier)

        return GameOdds(
            winProbability = probability,
            payoutMultiplier = rules.payoutMultiplier,
            expectedNetPoints = payoutCalculator.calculateExpectedNetPoints(bet, probability, payout),
        )
    }

    fun play(input: GuessNumberInput, rules: GuessNumberRules): GuessNumberResult {
        validateBet(input.bet, rules)
        require(input.guessedNumber in rules.minNumber..rules.maxNumber) {
            "Выбранное число должно находиться в диапазоне игры"
        }

        val winningPayout = payoutCalculator.calculatePayout(input.bet, rules.payoutMultiplier)
        val drawnNumber = randomNumberGenerator.nextIntInclusive(rules.minNumber, rules.maxNumber)
        check(drawnNumber in rules.minNumber..rules.maxNumber) {
            "Генератор случайных чисел вернул число вне диапазона игры"
        }
        val won = input.guessedNumber == drawnNumber

        return GuessNumberResult(
            guessedNumber = input.guessedNumber,
            drawnNumber = drawnNumber,
            won = won,
            bet = input.bet,
            payout = if (won) winningPayout else 0L,
        )
    }

    private fun validateBet(bet: Long, rules: GuessNumberRules) {
        require(bet in rules.minBet..rules.maxBet) {
            "Ставка должна находиться в диапазоне от ${rules.minBet} до ${rules.maxBet} поинтов"
        }
    }
}
