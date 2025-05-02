package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val location: String = "Kingston, Jamaica",
    val events: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedPriceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedDate: Date? = null,
    val isCategoriesLoading: Boolean = false
)

/**
 * ViewModel for the Search screen
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadEvents() // Now fetching real events
        loadCategories() // Fetch categories from the API
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
        
        // If query is not empty, search for events
        if (query.isNotEmpty()) {
            searchEvents(query)
        } else {
            // If query is empty, reset to all events
            _uiState.update { currentState ->
                currentState.copy(filteredEvents = currentState.events)
            }
        }
    }

    fun updateLocation(location: String) {
        _uiState.update { currentState ->
            currentState.copy(
                location = location
            )
        }
        // In a real app, this would trigger a new search with the updated location
    }

    fun selectCategory(categoryId: Int?) {
        _uiState.update { currentState ->
            currentState.copy(selectedCategoryId = categoryId)
        }
        
        // If a category is selected, filter events by category
        if (categoryId != null) {
            filterByCategory(categoryId)
        } else {
            // If no category is selected, reset to all events or current search results
            if (_uiState.value.searchQuery.isNotEmpty()) {
                searchEvents(_uiState.value.searchQuery)
            } else {
                _uiState.update { currentState ->
                    currentState.copy(filteredEvents = currentState.events)
                }
                applyFilters()
            }
        }
    }
    
    private fun filterByCategory(categoryId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                // Get the category name from the selected ID
                val categoryName = _uiState.value.categories.find { it.id == categoryId }?.name
                
                if (categoryName != null) {
                    // Filter events by category
                    eventRepository.getEventsByCategory(categoryName).collect { events ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                filteredEvents = events
                            )
                        }
                        
                        // Apply any other active filters
                        applyFilters()
                    }
                } else {
                    // If category name not found, reset to all events
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            filteredEvents = currentState.events
                        )
                    }
                    applyFilters()
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
    
    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPriceRange = range
            )
        }
        applyFilters()
    }

    fun updateDate(date: Date?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
        applyFilters()
    }

    fun resetFilters() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategoryId = null,
                selectedPriceRange = currentState.priceRange,
                selectedDate = null
            )
        }
        applyFilters()
    }

    fun refreshData() {
        loadEvents()
        loadCategories()
    }

    private fun loadEvents() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                eventRepository.getEvents().collect { events ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            events = events,
                            filteredEvents = events
                        )
                    }
                    
                    // Apply any active filters
                    applyFilters()
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                        events = emptyList(),
                        filteredEvents = emptyList()
                    )
                }
            }
        }
    }
    
    private fun searchEvents(query: String) {
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                eventRepository.searchEvents(query).collect { events ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            filteredEvents = events
                        )
                    }
                    
                    // Apply any other active filters to the search results
                    applyFilters()
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

    private fun applyFilters() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val allEvents = currentState.events
            
            val filtered = allEvents.filter { event ->
                // Filter by search query
                val matchesQuery = currentState.searchQuery.isEmpty() || 
                    event.title.contains(currentState.searchQuery, ignoreCase = true) ||
                    event.description.contains(currentState.searchQuery, ignoreCase = true)
                
                // Filter by category
                val matchesCategory = currentState.selectedCategoryId == null || 
                    event.category == currentState.categories.find { it.id == currentState.selectedCategoryId }?.name
                
                // Filter by price
                val matchesPrice = event.price >= currentState.selectedPriceRange.start && 
                    event.price <= currentState.selectedPriceRange.endInclusive
                
                // Filter by date
                val matchesDate = currentState.selectedDate == null || 
                    isSameDay(event.startDate, currentState.selectedDate)
                
                matchesQuery && matchesCategory && matchesPrice && matchesDate
            }
            
            _uiState.update { it.copy(filteredEvents = filtered) }
        }
    }

    private fun isSameDay(date1: Date, date2: Date?): Boolean {
        if (date2 == null) return false
        
        val cal1 = java.util.Calendar.getInstance().apply { time = date1 }
        val cal2 = java.util.Calendar.getInstance().apply { time = date2 }
        
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH)
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
}
