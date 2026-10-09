package com.codeandpray.luckygame.game.repository

import com.codeandpray.luckygame.game.entity.GameRound
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GameRoundRepository : JpaRepository<GameRound, Long> {
    fun findAllByUserIdOrderByCreatedAtDescIdDesc(
        userId: Long,
        pageable: Pageable,
    ): Page<GameRound>
}
