package com.codeandpray.luckygame.common.math.model

import java.math.BigDecimal

data class GameOdds(
    val winProbability: BigDecimal,
    val payoutMultiplier: BigDecimal,
    val expectedNetPoints: BigDecimal,
) {
    init {
        require(winProbability >= BigDecimal.ZERO && winProbability <= BigDecimal.ONE) {
            "Вероятность выигрыша должна находиться в диапазоне от 0 до 1"
        }
        require(payoutMultiplier > BigDecimal.ZERO) { "Коэффициент выплаты должен быть положительным" }
    }
}
