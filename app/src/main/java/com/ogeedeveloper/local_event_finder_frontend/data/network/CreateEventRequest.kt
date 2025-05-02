package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.google.gson.annotations.SerializedName

/**
 * Request data class for creating a new event
 */
data class CreateEventRequest(
    val title: String,
    val description: String,
    @SerializedName("categoryId")
    val category: String,
    val locationName: String,
    val longitude: Double,
    val latitude: Double,
    @SerializedName("location")
    val locationPoint: String? = null,  // Optional - only used if backend needs it directly
    @SerializedName("srid")
    val srid: Int = 4326,  // WGS84 coordinate system (standard for GPS)
    val dateTime: String,
    val endTime: String? = null,  // New field for event end time
    val price: Double,
    val coverImage: String,
    val totalSeats: Int? = null
)
