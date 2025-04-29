package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()
    
    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }
    
    fun updateVerificationCode(code: String) {
        _uiState.update { it.copy(verificationCode = code) }
    }
    
    fun updateNewPassword(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }
    
    fun updateConfirmPassword(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }
    
    fun updateResetToken(token: String) {
        _uiState.update { it.copy(resetToken = token) }
    }
    
    fun requestPasswordResetCode() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                val result = authRepository.requestPasswordReset(uiState.value.email)
                
                result.fold(
                    onSuccess = { message ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                isEmailSubmitted = true,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to send verification code"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to send verification code"
                    )
                }
            }
        }
    }
    
    fun verifyResetCode() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                val result = authRepository.verifyResetCode(
                    uiState.value.email, 
                    uiState.value.verificationCode
                )
                
                result.fold(
                    onSuccess = { resetToken ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                isCodeVerified = true,
                                resetToken = resetToken,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to verify code"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to verify code"
                    )
                }
            }
        }
    }
    
    fun resetPassword() {
        // Validate passwords match
        if (uiState.value.newPassword != uiState.value.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                val result = authRepository.resetPassword(
                    uiState.value.email,
                    uiState.value.resetToken,
                    uiState.value.newPassword
                )
                
                result.fold(
                    onSuccess = { success ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                isPasswordReset = success,
                                errorMessage = if (!success) "Failed to reset password" else null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to reset password"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to reset password"
                    )
                }
            }
        }
    }
    
    fun resendResetCode() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                
                val result = authRepository.resendResetCode(uiState.value.email)
                
                result.fold(
                    onSuccess = { message ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to resend code"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to resend code"
                    )
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = ResetPasswordUiState()
    }
}
