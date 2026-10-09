package com.codeandpray.luckygame.points.repository

import com.codeandpray.luckygame.points.entity.PointTransaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PointTransactionRepository : JpaRepository<PointTransaction, Long> {
    fun findAllByUserIdOrderByCreatedAtDescIdDesc(
        userId: Long,
        pageable: Pageable,
    ): Page<PointTransaction>
}
