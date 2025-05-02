package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for event-related API endpoints
 */
interface EventService {
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>
    
    @GET("events/{id}")
    suspend fun getEventById(@Path("id") eventId: String): Response<Event>
    
    @POST("events")
    suspend fun createEvent(@Body request: CreateEventRequest): Response<CreateEventResponse>
    
    @GET("events/nearby")
    suspend fun getNearbyEvents(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int
    ): Response<List<Event>>
    
    @GET("events")
    suspend fun getEventsByCategory(@Query("category") category: String): Response<List<Event>>
    
    @GET("events/categories")
    suspend fun getCategories(): Response<List<Category>>
}
