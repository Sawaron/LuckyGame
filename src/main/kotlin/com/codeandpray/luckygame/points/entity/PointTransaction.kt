package com.codeandpray.luckygame.points.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "point_transactions")
class PointTransaction(
    @field:Column(name = "user_id", nullable = false, updatable = false)
    val userId: Long,

    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false, updatable = false)
    val type: PointTransactionType,

    // Изменение баланса со знаком: ставка отрицательная, начисления положительные.
    @field:Column(nullable = false, updatable = false)
    val amount: Long,

    @field:Column(name = "round_id", updatable = false)
    val roundId: Long? = null,
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @field:Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    init {
        val transactionRoundId = roundId
        require(userId > 0) { "Идентификатор пользователя должен быть положительным" }
        require(transactionRoundId == null || transactionRoundId > 0) {
            "Идентификатор раунда должен быть положительным"
        }

        when (type) {
            PointTransactionType.INITIAL_GRANT -> {
                require(amount > 0) { "Стартовое начисление должно быть положительным" }
                require(transactionRoundId == null) { "Стартовое начисление не должно быть связано с раундом" }
            }

            PointTransactionType.BET -> {
                require(amount < 0) { "Сумма ставки в истории операций должна быть отрицательной" }
                require(transactionRoundId != null) { "Для списания ставки необходимо указать раунд" }
            }

            PointTransactionType.PAYOUT -> {
                require(amount > 0) { "Сумма выплаты должна быть положительной" }
                require(transactionRoundId != null) { "Для начисления выплаты необходимо указать раунд" }
            }
        }
    }
}
