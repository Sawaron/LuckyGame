package com.codeandpray.luckygame.user.repository

import com.codeandpray.luckygame.user.entity.User
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?

    // Вызывать внутри транзакции сервиса при изменении баланса пользователя.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    fun findByIdForUpdate(@Param("id") id: Long): User?
}
