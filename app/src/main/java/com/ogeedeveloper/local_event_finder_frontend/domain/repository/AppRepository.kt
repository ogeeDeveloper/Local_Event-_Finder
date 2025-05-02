package com.ogeedeveloper.local_event_finder_frontend.domain.repository

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Interface for user operations
 */
interface UserRepository {
    suspend fun updateUserProfile(user: User): Result<User>
    suspend fun updateUserInterests(interests: List<String>): Result<Boolean>
    suspend fun getUserInterests(): Flow<List<String>>
    suspend fun enableNotificationTypes(types: List<String>): Result<Boolean>
    suspend fun getNotificationTypes(): Flow<List<String>>
    suspend fun updateLocationPreference(useLocation: Boolean): Result<Boolean>
    suspend fun getLocationPreference(): Flow<Boolean>
}

/**
 * Interface for event operations
 */
interface EventRepository {
    suspend fun getEventsByCategory(category: String): Flow<List<com.ogeedeveloper.local_event_finder_frontend.domain.model.Event>>
    suspend fun getEventsByLocation(lat: Double, lng: Double, radius: Int): Flow<List<com.ogeedeveloper.local_event_finder_frontend.domain.model.Event>>
    suspend fun getEventDetails(eventId: String): Flow<com.ogeedeveloper.local_event_finder_frontend.domain.model.Event?>
    suspend fun searchEvents(query: String): Flow<List<com.ogeedeveloper.local_event_finder_frontend.domain.model.Event>>
    suspend fun saveEvent(eventId: String): Result<Boolean>
    suspend fun getSavedEvents(): Flow<List<Event>>
    suspend fun createEvent(
        title: String,
        description: String,
        category: String,
        locationName: String,
        latitude: Double,
        longitude: Double,
        dateTime: String,
        price: Double,
        coverImage: String?,
        totalSeats: Int
    ): Result<String>
    suspend fun getCategories(): Result<List<Category>>
}

// Basic domain model classes that would be defined in their own files
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)

//data class User(
//    // Add user properties here
//)