package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.text.format.DateUtils.formatDateTime
import android.util.Log
import com.ogeedeveloper.local_event_finder_frontend.data.cache.CategoryCache
import com.ogeedeveloper.local_event_finder_frontend.data.network.EventApi
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Location
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of EventRepository
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val categoryCache: CategoryCache
) : EventRepository {

    override suspend fun getEventsByCategory(category: String): Flow<List<Event>> {
        return flow {
            try {
                val events = if (category.equals("all", ignoreCase = true)) {
                    eventApi.getEvents()
                } else {
                    eventApi.getEventsByCategory(category)
                }
                emit(events)
            } catch (e: Exception) {
                // Log the error
                Log.e("EventRepository", "Error fetching events by category: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun getEventsByLocation(lat: Double, lng: Double, radius: Int): Flow<List<Event>> {
        return flow {
            try {
                // For now, we'll filter on the client side since there's no specific endpoint
                val events = eventApi.getEvents().filter { event ->
                    // Simple distance calculation (not accurate for long distances)
                    val latDiff = event.location?.latitude?.minus(lat) ?: 0.0
                    val lngDiff = event.location?.longitude?.minus(lng) ?: 0.0
                    val distanceSquared = latDiff * latDiff + lngDiff * lngDiff
                    distanceSquared <= radius * radius
                }
                emit(events)
            } catch (e: Exception) {
                Log.e("EventRepository", "Error fetching events by location: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun getEventDetails(eventId: String): Flow<Event?> {
        return flow {
            try {
                val event = eventApi.getEventById(eventId)
                emit(event)
            } catch (e: Exception) {
                Log.e("EventRepository", "Error fetching event by ID: ${e.message}")
                emit(null) // Emit null on error
            }
        }
    }

    override suspend fun searchEvents(query: String): Flow<List<Event>> {
        return flow {
            try {
                // If query is blank, just get all events
                val events = if (query.isBlank()) {
                    eventApi.getEvents()
                } else {
                    // For now, we'll filter on the client side since there's no specific search endpoint
                    eventApi.getEvents().filter {
                        it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
                    }
                }
                emit(events)
            } catch (e: Exception) {
                Log.e("EventRepository", "Error searching events: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun saveEvent(eventId: String): Result<Boolean> {
        return try {
            // In a real app, this would save to API and local storage
            delay(500)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSavedEvents(): Flow<List<Event>> {
        return flow {
            delay(800)
            // For demo, just return a subset of events
            emit(eventApi.getEvents().take(2))
        }
    }

    override suspend fun createEvent(
        title: String,
        description: String,
        category: String,
        locationName: String,
        latitude: Double,
        longitude: Double,
        dateTime: String,
        endTime: String?,
        price: Double,
        coverImage: String?,
        totalSeats: Int?
    ): Result<String> {
        return try {
            // Call the API to create the event
            val eventId = eventApi.createEvent(
                title = title,
                description = description,
                category = category,
                locationName = locationName,
                longitude = longitude,
                latitude = latitude,
                dateTime = dateTime,
                endTime = endTime,
                price = price,
                coverImage = coverImage ?: "",
                totalSeats = totalSeats
            )
            Result.success(eventId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCategories(): Result<List<Category>> {
        // First try to get categories from cache
        val cachedCategories = categoryCache.getCategories()
        
        // If cache is valid, return cached categories
        if (cachedCategories != null) {
            return Result.success(cachedCategories)
        }
        
        // Otherwise fetch from API and update cache
        return try {
            val categories = eventApi.getCategories()
            // Store in cache for future use
            categoryCache.storeCategories(categories)
            Result.success(categories)
        } catch (e: Exception) {
            // Fallback to default categories if API call fails
            Result.failure(e)
        }
    }
    
    override suspend fun refreshCategories(): Result<List<Category>> {
        return try {
            // Fetch fresh data from API, bypassing cache
            val categories = eventApi.getCategories()
            // Update cache with new data
            categoryCache.storeCategories(categories)
            Result.success(categories)
        } catch (e: Exception) {
            // Fallback to default categories if API call fails
            Result.failure(e)
        }
    }

    /**
     * Get all events from the API
     */
    override suspend fun getEvents(): Flow<List<Event>> {
        return flow {
            try {
                val events = eventApi.getEvents()
                emit(events)
            } catch (e: Exception) {
                Log.e("EventRepository", "Error fetching all events: ${e.message}")
                throw e
            }
        }
    }
}