package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API interface for event-related operations
 * Uses ApiConfig to determine the correct endpoints for different environments
 */
@Singleton
class EventApi @Inject constructor(private val apiConfig: ApiConfig) {
    suspend fun getEvents(): List<Event> {
        // In a real app, this would make an API call to apiConfig.getEventsUrl()
        // Example: retrofit.get(apiConfig.getEventsUrl())
        throw NotImplementedError("This would be implemented with Retrofit calling ${apiConfig.getEventsUrl()}")
    }
    
    suspend fun getEventById(eventId: String): Event {
        // In a real app, this would make an API call to apiConfig.getEventByIdUrl(eventId)
        // Example: retrofit.get(apiConfig.getEventByIdUrl(eventId))
        throw NotImplementedError("This would be implemented with Retrofit calling ${apiConfig.getEventByIdUrl(eventId)}")
    }
}