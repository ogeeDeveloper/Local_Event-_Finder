package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Event Details screen
 */
@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
    // private val eventRepository: EventRepository // Would be injected in a real app
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    init {
        val eventId: String = savedStateHandle.get<String>("eventId") ?: ""
        if (eventId.isNotEmpty()) {
            loadEvent(eventId)
        } else {
            _uiState.value = EventDetailsUiState.Error("Event ID not found")
        }
    }

    private fun loadEvent(eventId: String) {
        viewModelScope.launch {
            // In a real app, this would fetch from a repository
            // try {
            //     val event = eventRepository.getEventById(eventId)
            //     _uiState.value = EventDetailsUiState.Success(event)
            // } catch (e: Exception) {
            //     _uiState.value = EventDetailsUiState.Error(e.message ?: "Unknown error")
            // }
            
            // For now, we'll use dummy data
            _uiState.value = EventDetailsUiState.Success(getSampleEvent(eventId))
        }
    }
    
    // Sample data for development
    private fun getSampleEvent(eventId: String): Event {
        // For demo purposes, if the event ID ends with "free", return a free event
        return if (eventId.endsWith("free")) {
            Event(
                id = eventId,
                title = "Community Yoga Session",
                description = "Join our free community yoga session in the park. All levels welcome! Bring your own mat and water bottle. This event is part of our community wellness initiative to promote healthy living and mindfulness.",
                organizer = "Wellness Community",
                organizerLogoUrl = "https://i.imgur.com/JdUGbgd.jpg",
                startDate = java.util.Date(),
                endDate = java.util.Date(System.currentTimeMillis() + 5400000), // 1.5 hours later
                location = com.ogeedeveloper.local_event_finder_frontend.domain.model.Location(
                    id = "2",
                    name = "Central Park",
                    address = "New York, NY",
                    latitude = 40.785091,
                    longitude = -73.968285
                ),
                imageUrl = "https://i.imgur.com/JdUGbgd.jpg",
                price = 0.0, // Free event
                currency = "USD",
                category = "Wellness"
            )
        } else {
            Event(
                id = eventId,
                title = "Coldplay Ticket",
                description = "Coldplay are a British rock band formed in London in 1996. The band consists of vocalist, rhythm guitarist, and pianist Chris Martin, lead guitarist Jonny Buckland, bassist Guy Berryman, drummer Will Champion, and creative director Phil Harvey.",
                organizer = "Cold Play",
                organizerLogoUrl = "https://i.imgur.com/6Woi0Bf.jpg",
                startDate = java.util.Date(),
                endDate = java.util.Date(System.currentTimeMillis() + 3600000), // 1 hour later
                location = com.ogeedeveloper.local_event_finder_frontend.domain.model.Location(
                    id = "1",
                    name = "Wembley Stadium",
                    address = "London, UK",
                    latitude = 51.556,
                    longitude = -0.279
                ),
                imageUrl = "https://i.imgur.com/6Woi0Bf.jpg",
                price = 100.0,
                currency = "USD",
                category = "Entertainment"
            )
        }
    }
}

/**
 * UI state for the Event Details screen
 */
sealed class EventDetailsUiState {
    data object Loading : EventDetailsUiState()
    data class Success(val event: Event) : EventDetailsUiState()
    data class Error(val message: String) : EventDetailsUiState()
}
