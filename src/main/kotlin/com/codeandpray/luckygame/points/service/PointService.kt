package com.codeandpray.luckygame.points.service

import com.codeandpray.luckygame.common.dto.PageResponse
import com.codeandpray.luckygame.points.entity.PointTransaction
import com.codeandpray.luckygame.points.entity.PointTransactionType
import com.codeandpray.luckygame.points.repository.PointTransactionRepository
import com.codeandpray.luckygame.user.dto.PointTransactionResponse
import com.codeandpray.luckygame.user.entity.User
import com.codeandpray.luckygame.user.exception.UserNotFoundException
import com.codeandpray.luckygame.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Service
class PointService(
    private val pointTransactionRepository: PointTransactionRepository,
    private val userRepository: UserRepository
) {


    //	Увеличить баланс и записать INITIAL_GRANT без раунда
    @Transactional
    fun grantInitialPoints(user: User, amount: Long): Unit {
        user.creditPoints(amount)
        val transaction = PointTransaction(
            userId = user.id!!,
            type = PointTransactionType.INITIAL_GRANT,
            amount = amount,
            roundId = null
        )
        pointTransactionRepository.save(transaction)

        userRepository.save(user)
    }

    //	Принять положительную сумму, списать её и записать отрицательный BET
    @Transactional(propagation = Propagation.MANDATORY)
    fun debitBet(user: User, roundId: Long, amount: Long): Unit {

        user.debitPoints(amount)

        val transaction = PointTransaction(
            userId = user.id!!,
            type = PointTransactionType.BET,
            amount = -amount,
            roundId = roundId
        )
        pointTransactionRepository.save(transaction)

        userRepository.save(user)
    }

    //	Принять положительную сумму, начислить её и записать PAYOUT
    @Transactional(propagation = Propagation.MANDATORY)
    fun creditPayout(user: User, roundId: Long, amount: Long): Unit {

        user.creditPoints(amount)

        val transaction = PointTransaction(
            userId = user.id!!,
            type = PointTransactionType.PAYOUT,
            amount = amount,
            roundId = roundId
        )
        pointTransactionRepository.save(transaction)
        userRepository.save(user)
    }

@Transactional(readOnly = true)
    fun getHistory(userId: Long, pageable: Pageable): PageResponse<PointTransactionResponse> {
    if (!userRepository.existsById(userId)) {
        throw UserNotFoundException(userId)
    }
    val page = pointTransactionRepository.findAllByUserIdOrderByCreatedAtDescIdDesc(userId, pageable)

    val content = page.content.map { tx ->
        PointTransactionResponse(
            id = tx.id!!,
            roundId = tx.roundId,
            type = tx.type,
            amount = tx.amount,
            createdAt = tx.createdAt
        )
    }

    return PageResponse(
        items = content,
        page = page.number,
        size = page.size,
        totalElements = page.totalElements,
        totalPages = page.totalPages
    )
    }

}