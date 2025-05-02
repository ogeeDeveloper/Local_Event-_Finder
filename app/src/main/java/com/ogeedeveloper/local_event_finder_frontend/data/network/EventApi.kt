package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API interface for event-related operations
 * Uses ApiConfig to determine the correct endpoints for different environments
 */
@Singleton
class EventApi @Inject constructor(
    private val apiConfig: ApiConfig,
    private val retrofit: Retrofit
) {
    private val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }
    
    suspend fun getEvents(): List<Event> {
        val response = eventService.getEvents()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch events: ${response.errorBody()?.string()}")
        }
    }
    
    suspend fun getEventById(eventId: String): Event {
        val response = eventService.getEventById(eventId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Event not found")
        } else {
            throw Exception("Failed to fetch event: ${response.errorBody()?.string()}")
        }
    }

    suspend fun createEvent(
        title: String,
        description: String,
        category: String,
        locationName: String,
        longitude: Double,
        latitude: Double,
        dateTime: String,
        price: Double,
        coverImage: String,
        totalSeats: Int?
    ): String {
        val request = CreateEventRequest(
            title = title,
            description = description,
            category = category,
            locationName = locationName,
            longitude = longitude,
            latitude = latitude,
            dateTime = dateTime,
            price = price,
            coverImage = coverImage,
            totalSeats = totalSeats
        )
        
        val response = eventService.createEvent(request)
        if (response.isSuccessful) {
            return response.body()?.id ?: throw Exception("Failed to get event ID from response")
        } else {
            throw Exception("Failed to create event: ${response.errorBody()?.string()}")
        }
    }
    
    suspend fun getCategories(): List<Category> {
        val response = eventService.getCategories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch categories: ${response.errorBody()?.string()}")
        }
    }
}