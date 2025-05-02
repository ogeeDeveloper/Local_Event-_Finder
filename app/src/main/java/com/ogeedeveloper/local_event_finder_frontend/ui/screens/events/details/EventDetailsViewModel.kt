package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Event Details screen
 */
@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository
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

    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _uiState.value = EventDetailsUiState.Loading
            
            eventRepository.getEventDetails(eventId)
                .catch { e ->
                    _uiState.value = EventDetailsUiState.Error(e.message ?: "Failed to load event details")
                }
                .collect { event ->
                    if (event != null) {
                        _uiState.value = EventDetailsUiState.Success(event)
                    } else {
                        _uiState.value = EventDetailsUiState.Error("Event not found")
                    }
                }
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
