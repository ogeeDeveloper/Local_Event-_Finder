package com.ogeedeveloper.local_event_finder_frontend.domain.model

import java.util.Date

/**
 * Represents a user in the system
 */
data class User(
    val id: String = "",
    val uid: String = "", // Added to match the API response
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val profileImageUrl: String? = null,
    val interests: List<String> = emptyList(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val locationPreferences: LocationPreferences = LocationPreferences(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)