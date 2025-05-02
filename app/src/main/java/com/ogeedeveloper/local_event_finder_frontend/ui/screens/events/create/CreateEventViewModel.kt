package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.data.location.LocationService
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
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
    val category: Category? = null,
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
    val createdEventId: String? = null,
    
    // Categories
    val categories: List<Category> = emptyList(),
    val isCategoriesLoading: Boolean = false
)

/**
 * ViewModel for the Create Event flow
 */
@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val locationService: LocationService
) : ViewModel() {

    var uiState by mutableStateOf(CreateEventUiState())
        private set
        
    init {
        // Fetch categories when ViewModel is created
        fetchCategories()
    }
    
    // Fetch categories from the API
    private fun fetchCategories() {
        viewModelScope.launch {
            uiState = uiState.copy(isCategoriesLoading = true)
            
            try {
                val result = eventRepository.getCategories()
                result.fold(
                    onSuccess = { categories ->
                        uiState = uiState.copy(
                            categories = categories,
                            isCategoriesLoading = false
                        )
                    },
                    onFailure = { error ->
                        // Fallback to default categories if API call fails
                        uiState = uiState.copy(
                            categories = getDefaultCategories(),
                            isCategoriesLoading = false,
                            errorMessage = "Failed to load categories: ${error.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    categories = getDefaultCategories(),
                    isCategoriesLoading = false,
                    errorMessage = "An error occurred: ${e.message}"
                )
            }
        }
    }
    
    // Default categories as fallback
    private fun getDefaultCategories(): List<Category> {
        return listOf(
            Category(id = 1, name = "Music"),
            Category(id = 2, name = "Business"),
            Category(id = 3, name = "Food & Drink"),
            Category(id = 4, name = "Community"),
            Category(id = 5, name = "Arts"),
            Category(id = 6, name = "Film & Media"),
            Category(id = 7, name = "Sports & Fitness"),
            Category(id = 8, name = "Health"),
            Category(id = 9, name = "Science & Tech"),
            Category(id = 10, name = "Travel & Outdoor"),
            Category(id = 11, name = "Charity & Causes"),
            Category(id = 12, name = "Religion & Spirituality"),
            Category(id = 13, name = "Family & Education"),
            Category(id = 14, name = "Seasonal"),
            Category(id = 15, name = "Government"),
            Category(id = 16, name = "Fashion"),
            Category(id = 17, name = "Home & Lifestyle"),
            Category(id = 18, name = "Auto, Boat & Air"),
            Category(id = 19, name = "Hobbies"),
            Category(id = 20, name = "Other")
        )
    }

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

    fun updateCategory(category: Category?) {
        uiState = uiState.copy(category = category)
    }

    fun updateImageUri(uri: Uri?) {
        uiState = uiState.copy(imageUri = uri)
    }

    // Step 2: Location & Time
    fun updateLocation(location: String) {
        uiState = uiState.copy(location = location)
        
        // Geocode the address to get coordinates
        if (location.isNotBlank()) {
            viewModelScope.launch {
                val coordinates = locationService.geocodeAddress(location)
                if (coordinates != null) {
                    updateCoordinates(coordinates.first, coordinates.second)
                }
            }
        }
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
        if (uiState.category == null) {
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
        // Allow blank capacity (unlimited) or valid integer
        if (uiState.capacity.isNotBlank() && uiState.capacity.toIntOrNull() == null) {
            uiState = uiState.copy(errorMessage = "Please enter a valid capacity or leave blank for unlimited")
            return false
        }
        return true
    }

    // Helper method to format date and time for API
    private fun formatDateTime(): String {
        // Format: YYYY-MM-DD HH:MM:SS
        return "${uiState.date} ${uiState.startTime}:00"
    }
    
    // Helper method to format end time for API
    private fun formatEndTime(): String? {
        // If end time is empty, return null
        if (uiState.endTime.isBlank()) return null
        
        // Format: YYYY-MM-DD HH:MM:SS
        return "${uiState.date} ${uiState.endTime}:00"
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
                    
                    // Convert capacity to int, null if blank (unlimited)
                    val capacity = if (uiState.capacity.isBlank()) null else uiState.capacity.toIntOrNull()
                    
                    // Get image URL (in a real app, you would upload the image first and get a URL)
                    val imageUrl = uiState.imageUri?.toString() ?: ""
                    
                    // Format date and time
                    val dateTime = formatDateTime()
                    
                    // Format end time
                    val endTime = formatEndTime()
                    
                    // Ensure we have valid coordinates
                    if (!locationService.areCoordinatesValid(uiState.latitude, uiState.longitude)) {
                        // Try to geocode the address if coordinates are not valid
                        val coordinates = locationService.geocodeAddress(uiState.location)
                        if (coordinates != null) {
                            uiState = uiState.copy(
                                latitude = coordinates.first,
                                longitude = coordinates.second
                            )
                        } else {
                            uiState = uiState.copy(
                                isLoading = false,
                                errorMessage = "Could not determine location coordinates. Please enter a valid address."
                            )
                            return@launch
                        }
                    }
                    
                    // Call repository to create event
                    val result = eventRepository.createEvent(
                        title = uiState.title,
                        description = uiState.description,
                        category = uiState.category?.id?.toString() ?: "1", // Use category ID instead of name
                        locationName = uiState.location,
                        latitude = uiState.latitude,
                        longitude = uiState.longitude,
                        dateTime = dateTime,
                        endTime = endTime,
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
