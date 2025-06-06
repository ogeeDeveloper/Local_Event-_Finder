package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.VerificationStatus
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Verify Email screen
 */
data class VerifyEmailUiState(
    val email: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.NOT_STARTED,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verificationCode: String = ""
)

/**
 * ViewModel for the Verify Email screen
 */
@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailUiState())
    val uiState: StateFlow<VerifyEmailUiState> = _uiState.asStateFlow()

    init {
        // Get pending user data and extract email
        viewModelScope.launch {
            // In a real app, would get this from the repository
            _uiState.value = _uiState.value.copy(
                email = "tom.cruise@gmail.com"  // Demo data
            )

            // Send verification email automatically
            sendVerificationEmail()
        }
    }

    fun sendVerificationEmail() {
        val currentState = _uiState.value

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            verificationStatus = VerificationStatus.PENDING
        )

        viewModelScope.launch {
            val result = authRepository.sendEmailVerificationCode(currentState.email)

            result.fold(
                onSuccess = { message ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to send verification email"
                    )
                }
            )
        }
    }
    
    fun onVerificationCodeChanged(code: String) {
        _uiState.value = _uiState.value.copy(verificationCode = code)
    }
    
    fun verifyEmail() {
        val currentState = _uiState.value
        
        if (currentState.verificationCode.isEmpty()) {
            _uiState.value = currentState.copy(
                errorMessage = "Please enter the verification code"
            )
            return
        }
        
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            val result = authRepository.verifyEmail(
                email = currentState.email,
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

    fun checkVerificationStatus() {
        // In a real app, this would call the API to check if the email has been verified
        // For demo purposes, we'll just simulate success
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                verificationStatus = VerificationStatus.VERIFIED
            )
        }
    }
}