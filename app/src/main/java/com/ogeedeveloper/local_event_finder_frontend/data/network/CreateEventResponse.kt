package com.ogeedeveloper.local_event_finder_frontend.data.network

/**
 * Response data class for creating a new event
 */
data class CreateEventResponse(
    val id: String,
    val success: Boolean = true,
    val error: String? = null
)
