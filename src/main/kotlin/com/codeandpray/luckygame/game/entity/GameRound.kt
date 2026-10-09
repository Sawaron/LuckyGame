package com.codeandpray.luckygame.game.entity

import com.codeandpray.luckygame.game.guessnumber.entity.GuessNumberDetails
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "game_rounds")
class GameRound(
    @field:Column(name = "user_id", nullable = false, updatable = false)
    val userId: Long,

    @field:Enumerated(EnumType.STRING)
    @field:Column(name = "game_type", nullable = false, updatable = false)
    val gameType: GameType,

    @field:Column(nullable = false, updatable = false)
    val bet: Long,

    @field:Column(nullable = false, updatable = false)
    val payout: Long,

    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false, updatable = false)
    val outcome: RoundOutcome,

    @field:Embedded
    val guessNumberDetails: GuessNumberDetails? = null,
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @field:Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    init {
        require(userId > 0) { "Идентификатор пользователя должен быть положительным" }
        require(bet > 0) { "Ставка должна быть положительной" }
        require(payout >= 0) { "Выплата не может быть отрицательной" }
        require(outcome != RoundOutcome.LOSS || payout == 0L) {
            "При проигрыше выплата должна быть равна нулю"
        }

        when (gameType) {
            GameType.GUESS_NUMBER -> {
                val details = guessNumberDetails
                requireNotNull(details) { "Для угадывания числа необходимо указать параметры раунда" }
                val expectedOutcome = if (details.guessedNumber == details.drawnNumber) {
                    RoundOutcome.WIN
                } else {
                    RoundOutcome.LOSS
                }
                require(outcome == expectedOutcome) { "Результат раунда не соответствует выбранному и выпавшему числам" }
            }
        }
    }
}
