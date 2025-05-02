package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.text.format.DateUtils.formatDateTime
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

    // Mock data for events
    private val mockEvents = listOf(
        Event(
            id = "1",
            title = "Shawn Mendes The Virtual Tour in Germany 2021",
            description = "Join Shawn Mendes for an exclusive virtual concert experience",
            startDate = Date(),
            endDate = Date(),
            location = null,
            isOnline = true,
            joinUrl = "https://example.com/join",
            imageUrl = null,
            price = 100.0,
            originalPrice = 150.0,
            currency = "USD",
            category = "Music",
            tags = listOf("Concert", "Music", "Virtual")
        ),
        Event(
            id = "2",
            title = "Business Leadership Conference",
            description = "Learn from top industry leaders",
            startDate = Date(),
            endDate = Date(),
            location = Location(
                id = UUID.randomUUID().toString(),
                name = "Convention Center",
                address = "123 Main St",
                latitude = 40.7128,
                longitude = -74.0060
            ),
            isOnline = false,
            joinUrl = null,
            imageUrl = null,
            price = 250.0,
            originalPrice = null,
            currency = "USD",
            category = "Business",
            tags = listOf("Conference", "Business", "Leadership")
        ),
        Event(
            id = "3",
            title = "Food & Wine Festival",
            description = "Taste culinary delights from award-winning chefs",
            startDate = Date(),
            endDate = Date(),
            location = Location(
                id = UUID.randomUUID().toString(),
                name = "Downtown Park",
                address = "456 Park Ave",
                latitude = 40.7580,
                longitude = -73.9855
            ),
            isOnline = false,
            joinUrl = null,
            imageUrl = null,
            price = 75.0,
            originalPrice = null,
            currency = "USD",
            category = "Food",
            tags = listOf("Festival", "Food", "Wine")
        )
    )

    override suspend fun getEventsByCategory(category: String): Flow<List<Event>> {
        return flow {
            delay(800) // Simulate network delay
            if (category.equals("All", ignoreCase = true)) {
                emit(mockEvents)
            } else {
                emit(mockEvents.filter {
                    it.category.equals(category, ignoreCase = true)
                })
            }
        }
    }

    override suspend fun getEventsByLocation(lat: Double, lng: Double, radius: Int): Flow<List<Event>> {
        return flow {
            delay(1000)
            // In a real app, this would filter based on location coordinates
            emit(mockEvents)
        }
    }

    override suspend fun getEventDetails(eventId: String): Flow<Event?> {
        return flow {
            delay(800)
            emit(mockEvents.find { it.id == eventId })
        }
    }

    override suspend fun searchEvents(query: String): Flow<List<Event>> {
        return flow {
            delay(1000)
            if (query.isBlank()) {
                emit(mockEvents)
            } else {
                emit(mockEvents.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true)
                })
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
            emit(mockEvents.take(2))
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
}