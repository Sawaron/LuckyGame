package com.codeandpray.luckygame.common.exception

import com.codeandpray.luckygame.game.exception.InvalidGameRequestException
import com.codeandpray.luckygame.points.exception.InsufficientPointsException
import com.codeandpray.luckygame.user.exception.UserNotFoundException
import com.codeandpray.luckygame.user.exception.UsernameAlreadyExistsException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.Instant

// Перехватывает исключения из всех контроллеров и превращает их в ответ ApiError
@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // Имя уникального ограничения на колонку username, которое PostgreSQL создаёт по умолчанию
    private val usernameConstraint = "users_username_key"

    // Пользователь не найден: 404
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException, request: HttpServletRequest): ResponseEntity<ApiError> =
        buildError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.message.orEmpty(), request)

    // Имя пользователя уже занято: 409
    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExists(
        ex: UsernameAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", ex.message.orEmpty(), request)

    // Недостаточно поинтов для ставки: 409
    @ExceptionHandler(InsufficientPointsException::class)
    fun handleInsufficientPoints(
        ex: InsufficientPointsException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(HttpStatus.CONFLICT, "INSUFFICIENT_POINTS", ex.message.orEmpty(), request)

    // Запрос к игре нарушает правила: 400
    @ExceptionHandler(InvalidGameRequestException::class)
    fun handleInvalidGameRequest(
        ex: InvalidGameRequestException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(HttpStatus.BAD_REQUEST, "INVALID_GAME_REQUEST", ex.message.orEmpty(), request)

    // Не прошла проверка @Valid у тела запроса: 400, ошибки по полям попадают в fieldErrors
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ApiError> {
        val fieldErrors = ex.bindingResult.fieldErrors.associate { error ->
            error.field to (error.defaultMessage ?: "Некорректное значение")
        }
        return buildError(
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "Запрос содержит некорректные данные",
            request,
            fieldErrors
        )
    }

    // Не прошла проверка параметров пути или запроса, например @Positive у userId: 400
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleMethodValidation(
        ex: HandlerMethodValidationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Некорректные параметры запроса", request)

    // Тело запроса не читается: битый JSON или неверный тип поля: 400
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadableRequest(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST", "Некорректный формат запроса", request)

    // Параметр пути или запроса не приводится к нужному типу, например userId=abc: 400
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        buildError(
            HttpStatus.BAD_REQUEST,
            "INVALID_PARAMETER",
            "Неверное значение параметра '${ex.name}'",
            request
        )

    // Нарушение ограничений базы: 409 только для занятого имени, остальные нарушения — 500
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val databaseMessage = ex.mostSpecificCause.message.orEmpty()
        if (databaseMessage.contains(usernameConstraint, ignoreCase = true)) {
            return buildError(
                HttpStatus.CONFLICT,
                "USERNAME_ALREADY_EXISTS",
                "Имя пользователя уже занято",
                request
            )
        }
        logger.error("Нарушение целостности данных для ${request.requestURI}", ex)
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", INTERNAL_MESSAGE, request)
    }

    // Все остальные непредвиденные ошибки: 500, детали пишутся только в журнал
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiError> {
        logger.error("Непредвиденная ошибка для ${request.requestURI}", ex)
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", INTERNAL_MESSAGE, request)
    }

    // Собирает ответ с нужным статусом и телом ApiError
    private fun buildError(
        status: HttpStatus,
        code: String,
        message: String,
        request: HttpServletRequest,
        fieldErrors: Map<String, String> = emptyMap()
    ): ResponseEntity<ApiError> {
        val body = ApiError(
            code = code,
            message = message,
            path = request.requestURI,
            timestamp = Instant.now(),
            fieldErrors = fieldErrors
        )
        return ResponseEntity.status(status).body(body)
    }

    private companion object {
        // Общее сообщение для клиента, без внутренних деталей
        const val INTERNAL_MESSAGE = "Внутренняя ошибка сервера"
    }
}
