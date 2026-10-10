package com.codeandpray.luckygame.user.exception

class UserNotFoundException(val userId: Long) : RuntimeException("Пользователь с ID $userId не найден")