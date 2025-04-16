package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.location

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
 * UI state for the Location Permission screen
 */
data class LocationPermissionUiState(
    val hasLocationPermission: Boolean = false,
    val isLocationEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the Location Permission screen
 */
@HiltViewModel
class LocationPermissionViewModel @Inject constructor(
    private val permissionManager: PermissionManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationPermissionUiState())
    val uiState: StateFlow<LocationPermissionUiState> = _uiState.asStateFlow()

    init {
        // Check current location permission status
        checkLocationPermissionStatus()

        // Get user's saved location preference
        viewModelScope.launch {
            userRepository.getLocationPreference().collect { useLocation ->
                _uiState.value = _uiState.value.copy(
                    isLocationEnabled = useLocation
                )
            }
        }
    }

    fun checkLocationPermissionStatus() {
        val hasPermission = permissionManager.hasLocationPermission()
        val isEnabled = permissionManager.isLocationEnabled()

        _uiState.value = _uiState.value.copy(
            hasLocationPermission = hasPermission,
            isLocationEnabled = isEnabled
        )
    }

    fun enableLocation() {
        // Update user preference
        viewModelScope.launch {
            val result = userRepository.updateLocationPreference(true)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLocationEnabled = true,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Failed to update location preference"
                    )
                }
            )
        }
    }

    fun disableLocation() {
        viewModelScope.launch {
            val result = userRepository.updateLocationPreference(false)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLocationEnabled = false,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Failed to update location preference"
                    )
                }
            )
        }
    }
}
