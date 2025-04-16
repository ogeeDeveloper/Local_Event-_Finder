package com.ogeedeveloper.local_event_finder_frontend.domain.model

/**
 * Notification preferences for a user
 */
data class NotificationPreferences(
    val newEvents: Boolean = true,
    val priceDrops: Boolean = false,
    val friendEvents: Boolean = true,
    val eventReminders: Boolean = true
)
