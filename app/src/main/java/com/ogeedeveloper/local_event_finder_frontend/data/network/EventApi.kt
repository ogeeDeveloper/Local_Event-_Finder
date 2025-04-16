package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventApi @Inject constructor() {
    suspend fun getEvents(): List<Event> {
        throw NotImplementedError("This would be implemented with Retrofit")
    }
}