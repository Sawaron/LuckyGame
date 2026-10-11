package com.codeandpray.luckygame.points.dto

import com.codeandpray.luckygame.points.entity.PointTransactionType
import java.time.Instant

data class PointTransactionResponse (
    val id: Long,
    val roundId : Long?,
    val type: PointTransactionType,
    val amount: Long,
    val createdAt: Instant
)