package com.codeandpray.luckygame.points.exception

class InsufficientPointsException(val availablePoints: Long, val requiredPoints: Long) :
    RuntimeException("Недостаточно поинтов. Баланс: $availablePoints, требуется: $requiredPoints")