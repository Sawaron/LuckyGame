package com.codeandpray.luckygame.common.exception

import java.time.Instant

data class ApiError(
    val code: String,
    val message: String,
    val path: String,
    val timestamp: Instant,
    val fieldErrors: Map<String, String> = emptyMap()
)
