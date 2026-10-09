package com.codeandpray.luckygame.common.math

import java.math.BigDecimal
import java.math.RoundingMode

class PayoutCalculator {
    // Выплата включает возврат ставки; дробные поинты округляются вниз.
    fun calculatePayout(bet: Long, multiplier: BigDecimal): Long {
        require(bet > 0) { "Ставка должна быть положительной" }
        require(multiplier > BigDecimal.ZERO) { "Коэффициент выплаты должен быть положительным" }

        val payout = BigDecimal.valueOf(bet).multiply(multiplier).setScale(0, RoundingMode.DOWN)
        if (payout > BigDecimal.valueOf(Long.MAX_VALUE)) {
            throw ArithmeticException("Выплата превышает максимально допустимое количество поинтов")
        }
        return payout.longValueExact()
    }

    // Средний чистый результат учитывает списанную ставку и округлённую выплату.
    fun calculateExpectedNetPoints(bet: Long, winProbability: BigDecimal, payout: Long): BigDecimal {
        require(bet > 0) { "Ставка должна быть положительной" }
        require(winProbability >= BigDecimal.ZERO && winProbability <= BigDecimal.ONE) {
            "Вероятность выигрыша должна находиться в диапазоне от 0 до 1"
        }
        require(payout >= 0) { "Выплата не может быть отрицательной" }

        return winProbability.multiply(BigDecimal.valueOf(payout)).subtract(BigDecimal.valueOf(bet))
    }
}
