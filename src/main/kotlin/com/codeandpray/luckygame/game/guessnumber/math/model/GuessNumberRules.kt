package com.codeandpray.luckygame.game.guessnumber.math.model

import java.math.BigDecimal
import java.math.RoundingMode

data class GuessNumberRules(
    val minNumber: Int,
    val maxNumber: Int,
    val minBet: Long,
    val maxBet: Long,
    val payoutMultiplier: BigDecimal,
) {
    init {
        require(minNumber <= maxNumber) { "Минимальное число не должно превышать максимальное" }
        require(minBet > 0) { "Минимальная ставка должна быть положительной" }
        require(maxBet >= minBet) { "Максимальная ставка не должна быть меньше минимальной" }
        require(payoutMultiplier > BigDecimal.ZERO) { "Коэффициент выплаты должен быть положительным" }

        val normalizedMultiplier = payoutMultiplier.stripTrailingZeros()
        require(normalizedMultiplier.scale() <= 6 &&
            normalizedMultiplier.precision() - normalizedMultiplier.scale() <= 13) {
            "Коэффициент выплаты должен содержать не более 13 цифр до запятой и 6 после запятой"
        }

        val maximumPayout = BigDecimal.valueOf(maxBet)
            .multiply(payoutMultiplier).setScale(0, RoundingMode.DOWN)
        require(maximumPayout <= BigDecimal.valueOf(Long.MAX_VALUE)) {
            "Выплата по максимальной ставке превышает допустимое количество поинтов"
        }
    }
}
