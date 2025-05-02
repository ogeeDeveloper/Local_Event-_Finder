package com.ogeedeveloper.local_event_finder_frontend.data.network

/**
 * Request data class for creating a new event
 */
data class CreateEventRequest(
    val title: String,
    val description: String,
    val category: String,
    val locationName: String,
    val longitude: Double,
    val latitude: Double,
    val dateTime: String,
    val price: Double,
    val coverImage: String,
    val totalSeats: Int
)
