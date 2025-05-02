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
import java.util.Date
import javax.inject.Inject

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
                        val (upcoming, past) = events.partition { event ->
                            event.startDate.after(now) || event.startDate.time == now.time
                        }
                        
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
}
