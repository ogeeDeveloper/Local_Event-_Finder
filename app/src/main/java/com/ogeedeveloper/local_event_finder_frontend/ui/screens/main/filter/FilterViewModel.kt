package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.filter

import androidx.lifecycle.ViewModel
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

/**
 * UI state for the Filter screen
 */
data class FilterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val location: String = "South Jakarta",
    val date: Date? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedPriceRange: ClosedFloatingPointRange<Float> = 10f..25f
)

/**
 * ViewModel for the Filter screen
 */
@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        loadCategories() // In a real app, this would call a repository to fetch real data
    }

    fun updateLocation(location: String) {
        _uiState.update { currentState ->
            currentState.copy(
                location = location
            )
        }
    }

    fun updateDate(date: Date?) {
        _uiState.update { currentState ->
            currentState.copy(
                date = date
            )
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategoryId = categoryId
            )
        }
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPriceRange = range
            )
        }
    }

    fun resetFilters() {
        _uiState.update { currentState ->
            currentState.copy(
                location = "South Jakarta",
                date = null,
                selectedCategoryId = null,
                selectedPriceRange = 10f..25f
            )
        }
    }

    // This function would be replaced with a repository call in a real app
    fun getCategories(): List<Category> {
        return _uiState.value.categories
    }

    // Sample data functions - these would be removed in a real app with actual API calls
    private fun loadCategories() {
        _uiState.update { it.copy(isLoading = true) }
        
        // In a real app, this would be fetched from a repository
        val sampleCategories = getSampleCategories()
        
        _uiState.update { 
            it.copy(
                isLoading = false,
                categories = sampleCategories
            )
        }
    }

    private fun getSampleCategories(): List<Category> {
        return listOf(
            Category(id = "1", name = "Business"),
            Category(id = "2", name = "Festival"),
            Category(id = "3", name = "Music"),
            Category(id = "4", name = "Comedy"),
            Category(id = "5", name = "Concert"),
            Category(id = "6", name = "Workshop"),
            Category(id = "7", name = "Conference"),
            Category(id = "8", name = "Exhibition")
        )
    }
}
