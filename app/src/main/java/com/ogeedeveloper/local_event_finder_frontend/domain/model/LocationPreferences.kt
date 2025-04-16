package com.ogeedeveloper.local_event_finder_frontend.domain.model

/**
 * Location preferences for a user
 */
data class LocationPreferences(
    val useLocationServices: Boolean = false,
    val savedLocations: List<Location> = emptyList(),
    val primaryLocation: Location? = null
)