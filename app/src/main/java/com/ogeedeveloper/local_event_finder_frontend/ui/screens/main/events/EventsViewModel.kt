package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
class EventsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        loadEvents() // In a real app, this would call a repository to fetch real data
    }

    fun selectTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTabIndex = index
            )
        }
    }

    // This would be replaced with a repository call in a real app
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // In a real app, this would be fetched from a repository
            val upcomingEvents = getSampleUpcomingEvents()
            val pastEvents = getSamplePastEvents()
            
            _uiState.update { 
                it.copy(
                    isLoading = false,
                    upcomingEvents = upcomingEvents,
                    pastEvents = pastEvents
                )
            }
        }
    }

    // Sample data functions - these would be removed in a real app with actual API calls
    private fun getSampleUpcomingEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Shawn Mendes The Virtual Tour 2021 in Germany",
                description = "Join Shawn Mendes for a virtual concert experience",
                organizer = "Microsoft",
                startDate = Date(),
                endDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
                isOnline = true,
                price = 100.0,
                category = "Music"
            ),
            Event(
                id = "2",
                title = "Shawn Mendes The Virtual Tour 2021 in Germany",
                description = "Join Shawn Mendes for a virtual concert experience",
                organizer = "Microsoft",
                startDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
                endDate = Date(System.currentTimeMillis() + 4 * 60 * 60 * 1000),
                isOnline = true,
                price = 100.0,
                category = "Music"
            )
        )
    }

    private fun getSamplePastEvents(): List<Event> {
        return listOf(
            Event(
                id = "3",
                title = "Tech Conference 2025",
                description = "Annual tech conference with industry leaders",
                organizer = "TechCorp",
                startDate = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // Yesterday
                endDate = Date(System.currentTimeMillis() - 22 * 60 * 60 * 1000),
                price = 50.0,
                category = "Business"
            ),
            Event(
                id = "4",
                title = "Food Festival",
                description = "Explore cuisines from around the world",
                organizer = "FoodLovers",
                startDate = Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000), // 3 days ago
                endDate = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000),
                price = 25.0,
                category = "Food"
            )
        )
    }
}
