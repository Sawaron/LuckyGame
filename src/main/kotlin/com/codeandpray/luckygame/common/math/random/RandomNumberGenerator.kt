package com.codeandpray.luckygame.common.math.random

interface RandomNumberGenerator {
    // Обе границы диапазона включены.
    fun nextIntInclusive(min: Int, max: Int): Int
}
