package com.codeandpray.luckygame.game.service

import com.codeandpray.luckygame.common.dto.PageResponse
import com.codeandpray.luckygame.game.dto.GameRoundResponse
import com.codeandpray.luckygame.game.guessnumber.dto.GuessNumberDetailsResponse
import com.codeandpray.luckygame.game.repository.GameRoundRepository
import com.codeandpray.luckygame.user.exception.UserNotFoundException
import com.codeandpray.luckygame.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameHistoryService(
    private val gameRoundRepository: GameRoundRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getHistory(userId: Long, pageable: Pageable): PageResponse<GameRoundResponse> {
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException(userId)
        }

        val page = gameRoundRepository.findAllByUserIdOrderByCreatedAtDescIdDesc(userId, pageable)

        val content = page.content.map { round ->
            GameRoundResponse(
                id = requireNotNull(round.id),
                gameType = round.gameType,
                bet = round.bet,
                payout = round.payout,
                outcome = round.outcome,
                guessNumberDetails = round.guessNumberDetails?.let { d ->
                    GuessNumberDetailsResponse(
                        guessedNumber = d.guessedNumber,
                        drawnNumber = d.drawnNumber,
                        minNumber = d.minNumber,
                        maxNumber = d.maxNumber,
                        payoutMultiplier = d.payoutMultiplier
                    )
                },
                createdAt = round.createdAt
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