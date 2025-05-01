package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * UI state for the Create Event flow
 */
data class CreateEventUiState(
    val currentStep: Int = 1,
    
    // Step 1: Event Details
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val imageUri: Uri? = null,
    
    // Step 2: Location & Time
    val location: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    
    // Step 3: Tickets & Settings
    val isFreeEvent: Boolean = true,
    val price: String = "0.00",
    val capacity: String = "",
    val isPrivateEvent: Boolean = false,
    
    // Form validation
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the Create Event flow
 */
@HiltViewModel
class CreateEventViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(CreateEventUiState())
        private set

    // Navigation methods
    fun navigateToNextStep() {
        // For testing purposes, we'll allow navigation without validation
        uiState = uiState.copy(
            currentStep = minOf(uiState.currentStep + 1, 3),
            errorMessage = null
        )
    }

    fun navigateToPreviousStep() {
        uiState = uiState.copy(
            currentStep = maxOf(uiState.currentStep - 1, 1),
            errorMessage = null
        )
    }

    // Step 1: Event Details
    fun updateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun updateDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun updateCategory(category: String) {
        uiState = uiState.copy(category = category)
    }

    fun updateImageUri(uri: Uri?) {
        uiState = uiState.copy(imageUri = uri)
    }

    // Step 2: Location & Time
    fun updateLocation(location: String) {
        uiState = uiState.copy(location = location)
    }

    fun updateDate(date: String) {
        uiState = uiState.copy(date = date)
    }

    fun updateStartTime(time: String) {
        uiState = uiState.copy(startTime = time)
    }

    fun updateEndTime(time: String) {
        uiState = uiState.copy(endTime = time)
    }

    // Step 3: Tickets & Settings
    fun updateIsFreeEvent(isFree: Boolean) {
        uiState = uiState.copy(
            isFreeEvent = isFree,
            price = if (isFree) "0.00" else uiState.price
        )
    }

    fun updatePrice(price: String) {
        uiState = uiState.copy(price = price)
    }

    fun updateCapacity(capacity: String) {
        uiState = uiState.copy(capacity = capacity)
    }

    fun updateIsPrivateEvent(isPrivate: Boolean) {
        uiState = uiState.copy(isPrivateEvent = isPrivate)
    }

    // Form validation
    private fun validateCurrentStep(): Boolean {
        // For testing purposes, we'll return true for all steps
        return true
        
        /* Original validation logic - uncomment when ready for production
        return when (uiState.currentStep) {
            1 -> validateEventDetails()
            2 -> validateLocationTime()
            3 -> validateTicketsSettings()
            else -> true
        }
        */
    }

    private fun validateEventDetails(): Boolean {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(errorMessage = "Event title is required")
            return false
        }
        if (uiState.description.isBlank()) {
            uiState = uiState.copy(errorMessage = "Event description is required")
            return false
        }
        if (uiState.category.isBlank()) {
            uiState = uiState.copy(errorMessage = "Please select a category")
            return false
        }
        return true
    }

    private fun validateLocationTime(): Boolean {
        if (uiState.location.isBlank()) {
            uiState = uiState.copy(errorMessage = "Event location is required")
            return false
        }
        if (uiState.date.isBlank()) {
            uiState = uiState.copy(errorMessage = "Event date is required")
            return false
        }
        if (uiState.startTime.isBlank()) {
            uiState = uiState.copy(errorMessage = "Start time is required")
            return false
        }
        return true
    }

    private fun validateTicketsSettings(): Boolean {
        if (!uiState.isFreeEvent && uiState.price.toDoubleOrNull() == null) {
            uiState = uiState.copy(errorMessage = "Please enter a valid price")
            return false
        }
        return true
    }

    // Event creation methods
    fun saveEventDraft() {
        // In a real app, this would save the event to a repository
        uiState = uiState.copy(isLoading = true)
        
        // Simulate API call
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = null
        )
    }

    fun publishEvent() {
        if (validateCurrentStep()) {
            // In a real app, this would publish the event via a repository
            uiState = uiState.copy(isLoading = true)
            
            // Simulate API call
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = null
            )
        }
    }
}
