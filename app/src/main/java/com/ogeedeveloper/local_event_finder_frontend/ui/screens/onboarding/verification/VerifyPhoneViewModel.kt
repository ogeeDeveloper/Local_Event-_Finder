package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.VerificationStatus
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Verify Phone screen
 */
data class VerifyPhoneUiState(
    val phoneNumber: String = "",
    val userId: String = "",
    val verificationCode: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.NOT_STARTED,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val resendCountdown: Int = 30
)

/**
 * ViewModel for the Verify Phone screen
 */
@HiltViewModel
class VerifyPhoneViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyPhoneUiState())
    val uiState: StateFlow<VerifyPhoneUiState> = _uiState.asStateFlow()

    fun setPhoneNumberAndUserId(phoneNumber: String, userId: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phoneNumber,
            userId = userId
        )
        // Send verification code automatically when phone number and userId are set
        sendVerificationCode()
    }

    // Keep the old method for backward compatibility
    fun setPhoneNumber(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
        // Send verification code automatically when phone number is set
        sendVerificationCode()
    }

    init {
        // Start countdown for resend button
        startResendCountdown()
    }

    fun onVerificationCodeChanged(code: String) {
        _uiState.value = _uiState.value.copy(
            verificationCode = code,
            verificationStatus = if (code.length == 6) VerificationStatus.CODE_ENTERED else VerificationStatus.CODE_SENT
        )
    }

    fun verifyPhoneNumber() {
        val currentState = _uiState.value
        
        if (currentState.verificationCode.length != 6) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter a valid 6-digit verification code"
            )
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            try {
                // Call the API to verify phone number
                val result = authRepository.verifyPhoneNumber(
                    phoneNumber = currentState.phoneNumber,
                    code = currentState.verificationCode
                )
                
                result.fold(
                    onSuccess = {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            verificationStatus = VerificationStatus.VERIFIED,
                            errorMessage = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to verify phone number"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun sendVerificationCode() {
        val currentState = _uiState.value
        
        if (currentState.phoneNumber.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "Phone number is required"
            )
            return
        }
        
        if (currentState.userId.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "User ID is required"
            )
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            try {
                // Call the API to send verification code with userId
                val result = authRepository.sendPhoneVerificationCode(
                    phoneNumber = currentState.phoneNumber,
                    userId = currentState.userId
                )
                
                result.fold(
                    onSuccess = {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            verificationStatus = VerificationStatus.CODE_SENT,
                            errorMessage = null
                        )
                        // Start countdown for resend button
                        startResendCountdown()
                    },
                    onFailure = { exception ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to send verification code"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }
    
    private fun startResendCountdown() {
        viewModelScope.launch {
            var countdown = 30
            _uiState.value = _uiState.value.copy(resendCountdown = countdown)
            
            while (countdown > 0) {
                delay(1000) // Wait for 1 second
                countdown--
                _uiState.value = _uiState.value.copy(resendCountdown = countdown)
            }
        }
    }
}