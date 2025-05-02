package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
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
 * UI state for the Filter screen
 */
data class FilterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val location: String = "South Jakarta",
    val date: Date? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000f,
    val selectedPriceRange: ClosedFloatingPointRange<Float> = 10f..25f
)

/**
 * ViewModel for the Filter screen
 */
@HiltViewModel
class FilterViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        loadCategories() // Fetch categories from the API
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

    fun selectCategory(categoryId: Int?) {
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

    // Get categories from the current UI state
    fun getCategories(): List<Category> {
        return _uiState.value.categories
    }

    // Fetch categories from the API
    private fun loadCategories() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            try {
                val result = eventRepository.getCategories()
                result.fold(
                    onSuccess = { categories ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                categories = categories
                            )
                        }
                    },
                    onFailure = { error ->
                        // If API call fails, fall back to sample categories
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message,
                                categories = getSampleCategories()
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle any unexpected errors
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                        categories = getSampleCategories()
                    )
                }
            }
        }
    }

    // Fallback sample categories if API fails
    private fun getSampleCategories(): List<Category> {
        return listOf(
            Category(id = 1, name = "Business"),
            Category(id = 2, name = "Festival"),
            Category(id = 3, name = "Music"),
            Category(id = 4, name = "Comedy"),
            Category(id = 5, name = "Concert"),
            Category(id = 6, name = "Workshop"),
            Category(id = 7, name = "Conference"),
            Category(id = 8, name = "Exhibition")
        )
    }
}
