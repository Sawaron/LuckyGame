package com.codeandpray.luckygame.game.guessnumber.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.math.BigDecimal

@Embeddable
class GuessNumberDetails(
    @field:Column(name = "guessed_number", updatable = false)
    val guessedNumber: Int,

    @field:Column(name = "drawn_number", updatable = false)
    val drawnNumber: Int,

    @field:Column(name = "min_number", updatable = false)
    val minNumber: Int,

    @field:Column(name = "max_number", updatable = false)
    val maxNumber: Int,

    @field:Column(name = "payout_multiplier", precision = 19, scale = 6, updatable = false)
    val payoutMultiplier: BigDecimal,
) {
    init {
        require(minNumber <= maxNumber) { "Минимальное число не должно превышать максимальное" }
        require(guessedNumber in minNumber..maxNumber) { "Выбранное число должно находиться в диапазоне игры" }
        require(drawnNumber in minNumber..maxNumber) { "Выпавшее число должно находиться в диапазоне игры" }
        require(payoutMultiplier > BigDecimal.ZERO) { "Коэффициент выплаты должен быть положительным" }

        val normalizedMultiplier = payoutMultiplier.stripTrailingZeros()
        require(normalizedMultiplier.scale() <= 6 &&
            normalizedMultiplier.precision() - normalizedMultiplier.scale() <= 13) {
            "Коэффициент выплаты должен содержать не более 13 цифр до запятой и 6 после запятой"
        }
    }
}
