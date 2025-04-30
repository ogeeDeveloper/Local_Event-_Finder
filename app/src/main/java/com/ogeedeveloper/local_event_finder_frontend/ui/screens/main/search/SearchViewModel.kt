package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
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
    val selectedCategoryId: String? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedPriceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedDate: Date? = null
)

/**
 * ViewModel for the Search screen
 */
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadSampleData() // In a real app, this would call a repository to fetch real data
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query
            )
        }
        filterEvents()
    }

    fun updateLocation(location: String) {
        _uiState.update { currentState ->
            currentState.copy(
                location = location
            )
        }
        // In a real app, this would trigger a new search with the updated location
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategoryId = categoryId
            )
        }
        filterEvents()
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPriceRange = range
            )
        }
        filterEvents()
    }

    fun updateDate(date: Date?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
        filterEvents()
    }

    fun resetFilters() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategoryId = null,
                selectedPriceRange = currentState.priceRange,
                selectedDate = null
            )
        }
        filterEvents()
    }

    private fun filterEvents() {
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
                    event.category == currentState.selectedCategoryId
                
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

    // Sample data functions - these would be removed in a real app with actual API calls
    private fun loadSampleData() {
        _uiState.update { it.copy(isLoading = true) }
        
        // In a real app, this would be fetched from a repository
        val sampleEvents = getSampleEvents()
        val sampleCategories = getSampleCategories()
        
        // Calculate price range based on events
        val minPrice = sampleEvents.minOfOrNull { it.price }?.toFloat() ?: 0f
        val maxPrice = sampleEvents.maxOfOrNull { it.price }?.toFloat() ?: 1000f
        val priceRange = minPrice..maxPrice
        
        _uiState.update { 
            it.copy(
                isLoading = false,
                events = sampleEvents,
                filteredEvents = sampleEvents,
                categories = sampleCategories,
                priceRange = priceRange,
                selectedPriceRange = priceRange
            )
        }
    }

    private fun getSampleEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Shawn Mendes The Virtual Tour in Germany 2021",
                description = "Join Shawn Mendes for a virtual concert experience",
                organizer = "Coldplay Ticket",
                startDate = Date(),
                endDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
                isOnline = true,
                price = 100.0,
                originalPrice = 150.0,
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
            Category(id = "3", name = "Music"),
            Category(id = "4", name = "Comedy")
        )
    }
}
