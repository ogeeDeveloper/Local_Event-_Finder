package com.ogeedeveloper.local_event_finder_frontend.domain.model

/**
 * Represents a geographic location
 */
data class Location(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)