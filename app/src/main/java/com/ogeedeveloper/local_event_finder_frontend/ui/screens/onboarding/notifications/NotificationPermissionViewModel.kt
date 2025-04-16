package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.UserRepository
import com.ogeedeveloper.local_event_finder_frontend.util.permissions.PermissionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Notification Permission screen
 */
data class NotificationPermissionUiState(
    val hasNotificationPermission: Boolean = false,
    val selectedNotificationTypes: Set<String> = setOf("friend_events", "reminders"),
    val availableNotificationTypes: List<NotificationOption> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the Notification Permission screen
 */
@HiltViewModel
class NotificationPermissionViewModel @Inject constructor(
    private val permissionManager: PermissionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationPermissionUiState())
    val uiState: StateFlow<NotificationPermissionUiState> = _uiState.asStateFlow()

    init {
        // Check current notification permission status
        checkNotificationPermissionStatus()

        // Load notification options
        loadNotificationOptions()

        // Get user's saved notification preferences
        viewModelScope.launch {
            userRepository.getNotificationTypes().collect { types ->
                _uiState.value = _uiState.value.copy(
                    selectedNotificationTypes = types.toSet()
                )
            }
        }
    }

    private fun checkNotificationPermissionStatus() {
        val hasPermission = permissionManager.hasNotificationPermission()

        _uiState.value = _uiState.value.copy(
            hasNotificationPermission = hasPermission
        )
    }

    private fun loadNotificationOptions() {
        // In a real app, this might come from an API
        val options = listOf(
            NotificationOption(
                id = "new_events",
                title = "New events matching your interests",
                description = "Be the first to know about new events you might like"
            ),
            NotificationOption(
                id = "price_drops",
                title = "Price drops for saved events",
                description = "Get notified when tickets go on sale"
            ),
            NotificationOption(
                id = "friend_events",
                title = "Events friends are attending",
                description = "Find out what's popular in your social circle"
            ),
            NotificationOption(
                id = "reminders",
                title = "Reminders before booked events",
                description = "Never miss an event you've booked"
            )
        )

        _uiState.value = _uiState.value.copy(availableNotificationTypes = options)
    }

    fun toggleNotificationType(typeId: String) {
        val currentState = _uiState.value
        val currentSelected = currentState.selectedNotificationTypes

        val newSelected = if (currentSelected.contains(typeId)) {
            currentSelected - typeId
        } else {
            currentSelected + typeId
        }

        _uiState.value = currentState.copy(selectedNotificationTypes = newSelected)
    }

    fun saveNotificationPreferences() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = userRepository.enableNotificationTypes(
                currentState.selectedNotificationTypes.toList()
            )

            result.fold(
                onSuccess = {
                    _uiState.value = currentState.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to save notification preferences"
                    )
                }
            )
        }
    }
}