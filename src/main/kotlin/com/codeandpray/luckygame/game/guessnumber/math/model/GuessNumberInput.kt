package com.codeandpray.luckygame.game.guessnumber.math.model

data class GuessNumberInput(
    val guessedNumber: Int,
    val bet: Long,
) {
    init {
        require(bet > 0) { "Ставка должна быть положительной" }
    }
}
