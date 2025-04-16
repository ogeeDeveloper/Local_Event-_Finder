package com.ogeedeveloper.local_event_finder_frontend.domain.model

import java.util.Date

/**
 * Represents an event
 */
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val organizer: String = "",
    val organizerLogoUrl: String? = null,
    val startDate: Date = Date(),
    val endDate: Date? = null,
    val location: Location? = null,
    val isOnline: Boolean = false,
    val joinUrl: String? = null,
    val imageUrl: String? = null,
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val currency: String = "USD",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val attendees: Int = 0,
    val isSoldOut: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)