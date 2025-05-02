package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
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
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
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
    val errorMessage: String? = null,
    val isEventCreated: Boolean = false,
    val createdEventId: String? = null
)

/**
 * ViewModel for the Create Event flow
 */
@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    var uiState by mutableStateOf(CreateEventUiState())
        private set

    // Navigation methods
    fun navigateToNextStep() {
        if (validateCurrentStep()) {
            uiState = uiState.copy(
                currentStep = minOf(uiState.currentStep + 1, 3),
                errorMessage = null
            )
        }
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
    
    fun updateCoordinates(latitude: Double, longitude: Double) {
        uiState = uiState.copy(
            latitude = latitude,
            longitude = longitude
        )
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
        return when (uiState.currentStep) {
            1 -> validateEventDetails()
            2 -> validateLocationTime()
            3 -> validateTicketsSettings()
            else -> true
        }
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
        if (uiState.capacity.isBlank() || uiState.capacity.toIntOrNull() == null) {
            uiState = uiState.copy(errorMessage = "Please enter a valid capacity")
            return false
        }
        return true
    }

    // Helper method to format date and time for API
    private fun formatDateTime(): String {
        // Format: YYYY-MM-DD HH:MM:SS
        return "${uiState.date} ${uiState.startTime}:00"
    }

    // Event creation methods
    fun saveEventDraft() {
        // In a real app, this would save the event draft locally
        uiState = uiState.copy(isLoading = true)
        
        // Simulate saving
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = null
        )
    }

    fun publishEvent() {
        if (validateCurrentStep()) {
            viewModelScope.launch {
                uiState = uiState.copy(isLoading = true, errorMessage = null)
                
                try {
                    // Convert price to double
                    val price = if (uiState.isFreeEvent) 0.0 else uiState.price.toDoubleOrNull() ?: 0.0
                    
                    // Convert capacity to int
                    val capacity = uiState.capacity.toIntOrNull() ?: 0
                    
                    // Get image URL (in a real app, you would upload the image first and get a URL)
                    val imageUrl = uiState.imageUri?.toString() ?: ""
                    
                    // Format date and time
                    val dateTime = formatDateTime()
                    
                    // Call repository to create event
                    val result = eventRepository.createEvent(
                        title = uiState.title,
                        description = uiState.description,
                        category = uiState.category,
                        locationName = uiState.location,
                        latitude = uiState.latitude,
                        longitude = uiState.longitude,
                        dateTime = dateTime,
                        price = price,
                        coverImage = imageUrl,
                        totalSeats = capacity
                    )
                    
                    result.fold(
                        onSuccess = { eventId ->
                            uiState = uiState.copy(
                                isLoading = false,
                                errorMessage = null,
                                isEventCreated = true,
                                createdEventId = eventId
                            )
                        },
                        onFailure = { error ->
                            uiState = uiState.copy(
                                isLoading = false,
                                errorMessage = "Failed to create event: ${error.message}"
                            )
                        }
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "An error occurred: ${e.message}"
                    )
                }
            }
        }
    }
}
