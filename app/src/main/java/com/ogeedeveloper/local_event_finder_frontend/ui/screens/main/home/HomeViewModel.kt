package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null,
    val featuredEvents: List<Event> = emptyList(),
    val nearbyEvents: List<Event> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null
)

/**
 * ViewModel for the Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadSampleData() // In a real app, this would call a repository to fetch real data
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }

    private fun loadSampleData() {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )

        // In a real app, this would be fetched from a repository
        val sampleEvents = getSampleEvents()
        val sampleCategories = getSampleCategories()

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            featuredEvents = sampleEvents,
            nearbyEvents = sampleEvents,
            categories = sampleCategories
        )
    }

    fun selectCategory(categoryId: String?) {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = categoryId
        )
        
        // In a real app, this would filter events based on the selected category
        // For now, we'll just use the same sample data
    }

    // Sample data functions - these would be removed in a real app with actual API calls
    private fun getSampleEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Shawn Mendes The Virtual Tour in Germany 2021",
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
                title = "Tech Conference 2025",
                description = "Annual tech conference with industry leaders",
                organizer = "TechCorp",
                startDate = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), // Tomorrow
                endDate = Date(System.currentTimeMillis() + 26 * 60 * 60 * 1000),
                price = 50.0,
                category = "Business"
            ),
            Event(
                id = "3",
                title = "Food Festival",
                description = "Explore cuisines from around the world",
                organizer = "FoodLovers",
                startDate = Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000), // 3 days later
                endDate = Date(System.currentTimeMillis() + 4 * 24 * 60 * 60 * 1000),
                price = 25.0,
                category = "Food"
            )
        )
    }

    private fun getSampleCategories(): List<Category> {
        return listOf(
            Category(id = "1", name = "Business"),
            Category(id = "2", name = "Festival"),
            Category(id = "3", name = "Festival"),
            Category(id = "4", name = "Comedy")
        )
    }
}
