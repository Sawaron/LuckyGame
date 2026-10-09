package com.codeandpray.luckygame.game.guessnumber.math.model

data class GuessNumberResult(
    val guessedNumber: Int,
    val drawnNumber: Int,
    val won: Boolean,
    val bet: Long,
    val payout: Long,
) {
    init {
        require(bet > 0) { "Ставка должна быть положительной" }
        require(payout >= 0) { "Выплата не может быть отрицательной" }
        require(won == (guessedNumber == drawnNumber)) {
            "Результат игры не соответствует выбранному и выпавшему числам"
        }
        require(won || payout == 0L) { "При проигрыше выплата должна быть равна нулю" }
    }
}
