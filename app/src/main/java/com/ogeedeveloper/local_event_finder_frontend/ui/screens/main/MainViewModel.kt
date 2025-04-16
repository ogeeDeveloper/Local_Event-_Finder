package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the main app screens
 */
data class MainAppUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val featuredEvents: List<Event> = emptyList(),
    val nearbyEvents: List<Event> = emptyList(),
    val recommendedEvents: List<Event> = emptyList(),
    val savedEvents: List<Event> = emptyList(),
    val selectedCategory: String = "All",
    val selectedBottomTab: Int = 0
)

/**
 * ViewModel for the main app screens
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainAppUiState())
    val uiState: StateFlow<MainAppUiState> = _uiState.asStateFlow()

    init {
        // Load initial data
        loadData()
        // Set up user data flow
        observeUserData()
        // Load featured and nearby events
        loadFeaturedEvents()
        loadNearbyEvents()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }

    private fun loadFeaturedEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getEventsByCategory("featured").collect { events ->
                    // Convert domain.repository.Event to domain.model.Event
                    val modelEvents = events.map { repoEvent ->
                        Event(
                            id = repoEvent.id,
                            title = repoEvent.title,
                            description = repoEvent.description,
                            startDate = java.util.Date(), // Use appropriate date conversion
                            price = repoEvent.price
                            // Map other properties as needed
                        )
                    }
                    _uiState.value = _uiState.value.copy(featuredEvents = modelEvents)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error loading featured events"
                )
            }
        }
    }

    private fun loadNearbyEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getEventsByLocation(0.0, 0.0, 10).collect { events ->
                    // Convert domain.repository.Event to domain.model.Event
                    val modelEvents = events.map { repoEvent ->
                        Event(
                            id = repoEvent.id,
                            title = repoEvent.title,
                            description = repoEvent.description,
                            startDate = java.util.Date(), // Use appropriate date conversion
                            price = repoEvent.price
                            // Map other properties as needed
                        )
                    }
                    _uiState.value = _uiState.value.copy(nearbyEvents = modelEvents)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error loading nearby events"
                )
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // Load saved events
                eventRepository.getSavedEvents().collect { events ->
                    // Convert domain.repository.Event to domain.model.Event
                    val modelEvents = events.map { repoEvent ->
                        Event(
                            id = repoEvent.id,
                            title = repoEvent.title,
                            description = repoEvent.description,
                            startDate = java.util.Date(), // Use appropriate date conversion
                            price = repoEvent.price
                            // Map other properties as needed
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        savedEvents = modelEvents,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error loading data"
                )
            }
        }
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            eventRepository.searchEvents(query).collect { events ->
                // Convert domain.repository.Event to domain.model.Event
                val modelEvents = events.map { repoEvent ->
                    Event(
                        id = repoEvent.id,
                        title = repoEvent.title,
                        description = repoEvent.description,
                        startDate = java.util.Date(), // Use appropriate date conversion
                        price = repoEvent.price
                        // Map other properties as needed
                    )
                }
                _uiState.value = _uiState.value.copy(
                    recommendedEvents = modelEvents,
                    isLoading = false
                )
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            eventRepository.getEventsByCategory(category).collect { events ->
                // Convert domain.repository.Event to domain.model.Event
                val modelEvents = events.map { repoEvent ->
                    Event(
                        id = repoEvent.id,
                        title = repoEvent.title,
                        description = repoEvent.description,
                        startDate = java.util.Date(), // Use appropriate date conversion
                        price = repoEvent.price
                        // Map other properties as needed
                    )
                }
                _uiState.value = _uiState.value.copy(
                    recommendedEvents = modelEvents,
                    isLoading = false
                )
            }
        }
    }

    fun saveEvent(eventId: String) {
        viewModelScope.launch {
            val result = eventRepository.saveEvent(eventId)

            // If successful, refresh saved events
            if (result.isSuccess) {
                loadData() // Reuse existing function to reload saved events
            }
        }
    }

    fun selectBottomTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedBottomTab = tabIndex)
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            // Navigation handled by UI
        }
    }
}