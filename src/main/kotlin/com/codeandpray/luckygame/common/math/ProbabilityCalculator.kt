package com.codeandpray.luckygame.common.math

import java.math.BigDecimal
import java.math.RoundingMode

class ProbabilityCalculator {
    // Вероятность задаётся долей от 0 до 1, с точностью до 18 знаков после запятой.
    fun calculate(favorableOutcomes: Long, totalOutcomes: Long): BigDecimal {
        require(totalOutcomes > 0) { "Количество возможных исходов должно быть положительным" }
        require(favorableOutcomes in 0..totalOutcomes) {
            "Количество выигрышных исходов должно быть от нуля до количества возможных исходов"
        }
        return BigDecimal.valueOf(favorableOutcomes)
            .divide(BigDecimal.valueOf(totalOutcomes), 18, RoundingMode.HALF_EVEN)
    }
}
