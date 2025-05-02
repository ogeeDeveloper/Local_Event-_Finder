package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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
    val selectedCategoryId: Int? = null,
    val isCategoriesLoading: Boolean = false
)

/**
 * ViewModel for the Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadEvents() // Now fetching real events
        loadCategories() // Fetch categories from the API
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                _uiState.update { currentState ->
                    currentState.copy(currentUser = user)
                }
            }
        }
    }

    private fun loadEvents() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                // Fetch featured events (we'll use all events for now)
                eventRepository.getEvents().collectLatest { events ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            featuredEvents = events.take(5), // Take first 5 for featured
                            nearbyEvents = events // All events for nearby
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                        // Use empty lists instead of sample data
                        featuredEvents = emptyList(),
                        nearbyEvents = emptyList()
                    )
                }
            }
        }
    }
    
    // Load categories from the API
    private fun loadCategories() {
        _uiState.update { it.copy(isCategoriesLoading = true) }
        
        viewModelScope.launch {
            try {
                val result = eventRepository.getCategories()
                result.fold(
                    onSuccess = { categories ->
                        _uiState.update { 
                            it.copy(
                                categories = categories,
                                isCategoriesLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                errorMessage = error.message,
                                isCategoriesLoading = false,
                                categories = emptyList()
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        errorMessage = e.message,
                        isCategoriesLoading = false,
                        categories = emptyList()
                    )
                }
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, isLoading = true) }
        
        viewModelScope.launch {
            try {
                if (categoryId == null) {
                    // If no category selected, load all events
                    eventRepository.getEvents().collectLatest { events ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                featuredEvents = events.take(5),
                                nearbyEvents = events
                            )
                        }
                    }
                } else {
                    // Get the category name from the selected ID
                    val categoryName = _uiState.value.categories.find { it.id == categoryId }?.name ?: ""
                    
                    // Load events for the selected category
                    eventRepository.getEventsByCategory(categoryName).collectLatest { events ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                featuredEvents = events.take(5),
                                nearbyEvents = events
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun refreshData() {
        loadEvents()
        loadCategories()
    }
}
