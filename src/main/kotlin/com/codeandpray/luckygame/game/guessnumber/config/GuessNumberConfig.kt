package com.codeandpray.luckygame.game.guessnumber.config

import com.codeandpray.luckygame.common.math.PayoutCalculator
import com.codeandpray.luckygame.common.math.ProbabilityCalculator
import com.codeandpray.luckygame.common.math.random.RandomNumberGenerator
import com.codeandpray.luckygame.game.guessnumber.math.GuessNumberEngine
import com.codeandpray.luckygame.game.guessnumber.math.model.GuessNumberRules
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration(proxyBeanMethods = false)
class GuessNumberConfig {
    // Начальные правила можно переопределить через настройки приложения.
    @Bean
    fun guessNumberRules(
        @Value("\${games.guess-number.min-number:1}") minNumber: Int,
        @Value("\${games.guess-number.max-number:10}") maxNumber: Int,
        @Value("\${games.guess-number.min-bet:1}") minBet: Long,
        @Value("\${games.guess-number.max-bet:100}") maxBet: Long,
        @Value("\${games.guess-number.payout-multiplier:10}") payoutMultiplier: BigDecimal,
    ): GuessNumberRules = GuessNumberRules(
        minNumber = minNumber,
        maxNumber = maxNumber,
        minBet = minBet,
        maxBet = maxBet,
        payoutMultiplier = payoutMultiplier,
    )

    @Bean
    fun guessNumberEngine(
        probabilityCalculator: ProbabilityCalculator,
        payoutCalculator: PayoutCalculator,
        randomNumberGenerator: RandomNumberGenerator,
    ): GuessNumberEngine = GuessNumberEngine(
        probabilityCalculator = probabilityCalculator,
        payoutCalculator = payoutCalculator,
        randomNumberGenerator = randomNumberGenerator,
    )
}
