package com.codeandpray.luckygame.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
class User(
    @field:Column(nullable = false, unique = true, updatable = false, length = 255)
    val username: String,
    points: Long = 0,
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @field:Column(nullable = false)
    var points: Long = points
        protected set

    @field:Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    init {
        require(username.isNotBlank()) { "Имя пользователя не должно быть пустым" }
        require(username.length <= 255) { "Имя пользователя не должно превышать 255 символов" }
        require(points >= 0) { "Баланс поинтов не может быть отрицательным" }
    }

    fun debitPoints(amount: Long) {
        require(amount > 0) { "Сумма списания должна быть положительной" }
        require(points >= amount) { "Недостаточно поинтов для списания" }
        points -= amount
    }

    fun creditPoints(amount: Long) {
        require(amount > 0) { "Сумма начисления должна быть положительной" }
        if (amount > Long.MAX_VALUE - points) {
            throw ArithmeticException("Начисление превышает максимально допустимый баланс поинтов")
        }
        points += amount
    }
}
