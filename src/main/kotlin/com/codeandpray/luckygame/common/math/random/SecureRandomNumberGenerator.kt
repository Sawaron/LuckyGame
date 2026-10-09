package com.codeandpray.luckygame.common.math.random

import java.security.SecureRandom

class SecureRandomNumberGenerator(
    private val random: SecureRandom = SecureRandom(),
) : RandomNumberGenerator {
    override fun nextIntInclusive(min: Int, max: Int): Int {
        require(min <= max) { "Минимальное число не должно превышать максимальное" }

        // Long позволяет включить Int.MAX_VALUE без переполнения верхней границы.
        return random.nextLong(min.toLong(), max.toLong() + 1).toInt()
    }
}
