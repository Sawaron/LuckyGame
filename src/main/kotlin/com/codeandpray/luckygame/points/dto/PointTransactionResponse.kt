package com.codeandpray.luckygame.user.dto

import com.codeandpray.luckygame.points.entity.PointTransactionType
import java.time.Instant

data class PointTransactionResponse (
    val id: Long,
    val roundId : Long?,
    val type: PointTransactionType,
    val amount: Long,
    val createdAt: Instant
)