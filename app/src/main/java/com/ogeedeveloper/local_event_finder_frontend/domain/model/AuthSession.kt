package com.ogeedeveloper.local_event_finder_frontend.domain.model

import java.util.Date

/**
 * Represents an authenticated session
 */
data class AuthSession(
    val userId: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresAt: Date = Date()
)