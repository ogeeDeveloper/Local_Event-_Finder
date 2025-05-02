package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import android.util.Log

/**
 * UI state for the Events screen
 */
data class EventsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null,
    val upcomingEvents: List<Event> = emptyList(),
    val pastEvents: List<Event> = emptyList(),
    val selectedTabIndex: Int = 0
)

/**
 * ViewModel for the Events screen
 */
@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun selectTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTabIndex = index
            )
        }
    }

    fun refreshEvents() {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // Load all events
                eventRepository.getEventsByCategory("all")
                    .catch { e ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "Failed to load events"
                            )
                        }
                    }
                    .collect { events ->
                        // Split events into upcoming and past based on their start date
                        val now = Date()
                        Log.d("EventsViewModel", "Current date: $now")
                        
                        val (upcoming, past) = events.partition { event ->
                            Log.d("EventsViewModel", "Event ${event.title} date: ${event.startDate}")
                            event.startDate.after(now) || isSameDay(event.startDate, now)
                        }
                        
                        Log.d("EventsViewModel", "Upcoming events: ${upcoming.size}, Past events: ${past.size}")
                        
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                upcomingEvents = upcoming,
                                pastEvents = past
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    /**
     * Helper function to check if two dates are on the same day
     */
    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}
