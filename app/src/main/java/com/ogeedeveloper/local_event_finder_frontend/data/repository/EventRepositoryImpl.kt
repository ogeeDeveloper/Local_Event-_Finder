package com.ogeedeveloper.local_event_finder_frontend.data.repository

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of EventRepository
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi
) : EventRepository {

    // Mock data for events
    private val mockEvents = listOf(
        Event(
            id = "1",
            title = "Shawn Mendes The Virtual Tour in Germany 2021",
            description = "Join Shawn Mendes for an exclusive virtual concert experience",
            date = "Dec 2, 2021",
            time = "10:00 - 12:00",
            location = "Virtual Event",
            price = 100.0,
            category = "Music"
        ),
        Event(
            id = "2",
            title = "Business Leadership Conference",
            description = "Learn from top industry leaders",
            date = "Dec 15, 2021",
            time = "9:00 - 17:00",
            location = "Convention Center",
            price = 250.0,
            category = "Business"
        ),
        Event(
            id = "3",
            title = "Food & Wine Festival",
            description = "Taste culinary delights from award-winning chefs",
            date = "Dec 10, 2021",
            time = "18:00 - 22:00",
            location = "Downtown Park",
            price = 75.0,
            category = "Food"
        )
    )

    override suspend fun getEventsByCategory(category: String): Flow<List<Event>> {
        return flow {
            delay(800) // Simulate network delay
            emit(mockEvents.filter {
                it.category.equals(category, ignoreCase = true)
            })
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
}