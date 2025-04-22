package com.ogeedeveloper.local_event_finder_frontend.domain.model

/**
 * Response model for login API
 * Contains the authentication token and user information
 */
data class LoginResponse(
    val token: String,
    val user: User
)
