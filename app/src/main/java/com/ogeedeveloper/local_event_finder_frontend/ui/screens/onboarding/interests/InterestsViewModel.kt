package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Interests screen
 */
data class InterestsUiState(
    val availableInterests: List<InterestCategory> = emptyList(),
    val selectedInterests: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the Interests screen
 */
@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestsUiState())
    val uiState: StateFlow<InterestsUiState> = _uiState.asStateFlow()

    init {
        // Load available interest categories
        loadInterestCategories()

        // Load user's saved interests if any
        viewModelScope.launch {
            userRepository.getUserInterests().collect { interests ->
                _uiState.value = _uiState.value.copy(
                    selectedInterests = interests.toSet()
                )
            }
        }
    }

    private fun loadInterestCategories() {
        // Mock data for available interests
        val interests = listOf(
            InterestCategory("music", "Music & Concerts"),
            InterestCategory("sports", "Sports & Fitness"),
            InterestCategory("food", "Food & Drinks"),
            InterestCategory("business", "Business"),
            InterestCategory("education", "Education"),
            InterestCategory("art", "Art & Culture"),
            InterestCategory("technology", "Technology"),
            InterestCategory("outdoors", "Outdoors")
        )

        _uiState.value = _uiState.value.copy(availableInterests = interests)
    }

    fun toggleInterest(interestId: String) {
        val currentState = _uiState.value
        val currentSelected = currentState.selectedInterests

        val newSelected = if (currentSelected.contains(interestId)) {
            currentSelected - interestId
        } else {
            currentSelected + interestId
        }

        _uiState.value = currentState.copy(selectedInterests = newSelected)
    }

    fun saveInterests() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = userRepository.updateUserInterests(currentState.selectedInterests.toList())

            result.fold(
                onSuccess = {
                    _uiState.value = currentState.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to save interests"
                    )
                }
            )
        }
    }
}