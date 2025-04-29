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

    init {
        // Get current user data and extract phone number
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                user?.let {
                    _uiState.value = _uiState.value.copy(
                        phoneNumber = it.phoneNumber
                    )
                    
                    // Start countdown for resend button
                    startResendCountdown()
                    
                    // Send verification code automatically
                    sendVerificationCode()
                }
            }
        }
    }

    fun onVerificationCodeChanged(code: String) {
        if (code.length <= 6) {  // Limit to 6 digits
            _uiState.value = _uiState.value.copy(verificationCode = code)

            // Auto-verify when 6 digits are entered
            if (code.length == 6) {
                verifyCode()
            }
        }
    }

    fun verifyCode() {
        val currentState = _uiState.value

        if (currentState.verificationCode.length != 6) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter the 6-digit verification code"
            )
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = authRepository.verifyPhoneNumber(
                phoneNumber = currentState.phoneNumber,
                code = currentState.verificationCode
            )

            result.fold(
                onSuccess = { message ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        verificationStatus = VerificationStatus.VERIFIED,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        verificationStatus = VerificationStatus.FAILED,
                        errorMessage = exception.message ?: "Verification failed"
                    )
                }
            )
        }
    }

    fun sendVerificationCode() {
        val currentState = _uiState.value
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            verificationStatus = VerificationStatus.PENDING
        )
        
        viewModelScope.launch {
            val result = authRepository.sendPhoneVerificationCode(currentState.phoneNumber)
            
            result.fold(
                onSuccess = { message ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                    startResendCountdown()
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to send verification code"
                    )
                }
            )
        }
    }
    
    private fun startResendCountdown() {
        viewModelScope.launch {
            var countdown = 30
            while (countdown > 0) {
                _uiState.value = _uiState.value.copy(resendCountdown = countdown)
                delay(1000)
                countdown--
            }
            _uiState.value = _uiState.value.copy(resendCountdown = 0)
        }
    }
}