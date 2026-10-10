package com.codeandpray.luckygame.user.exception

// В документации (раздел 16) класс называется UsernameAlreadyExistsException
class UsernameAlreadyExistsException(val username: String) : RuntimeException("Имя пользователя '$username' уже занято")