package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
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
    @GET("api/events")
    suspend fun getEvents(): Response<List<Event>>
    
    @GET("api/events/{id}")
    suspend fun getEventById(@Path("id") eventId: String): Response<Event>
    
    @POST("api/events")
    suspend fun createEvent(@Body request: CreateEventRequest): Response<CreateEventResponse>
    
    @GET("api/events/nearby")
    suspend fun getNearbyEvents(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int
    ): Response<List<Event>>
    
    @GET("api/events/category/{category}")
    suspend fun getEventsByCategory(@Path("category") category: String): Response<List<Event>>
}
