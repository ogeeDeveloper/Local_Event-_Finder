package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Login screen
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordVisible: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false
)

/**
 * ViewModel for the Login screen
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            isEmailError = !isValidEmail(email) && email.isNotEmpty(),
            errorMessage = null
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isPasswordError = password.isEmpty(),
            errorMessage = null
        )
    }

    fun onRememberMeChanged(rememberMe: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = rememberMe)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun login() {
        val currentState = _uiState.value

        // Validate fields
        if (!validateFields()) {
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = authRepository.signIn(
                email = currentState.email,
                password = currentState.password
            )

            result.fold(
                onSuccess = {
                    _uiState.value = currentState.copy(isLoading = false)
                    // Login successful - navigation handled by UI
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Login failed"
                    )
                }
            )
        }
    }

    fun getGoogleSignInIntent(): Intent {
        return authRepository.getGoogleSignInIntent()
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                viewModelScope.launch {
                    val result = authRepository.signInWithGoogle(idToken)

                    result.fold(
                        onSuccess = {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                email = it.email // Set the email from the Google account
                            )
                            // Login successful - navigation handled by UI
                        },
                        onFailure = { exception ->
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                errorMessage = exception.message ?: "Google Sign-In failed"
                            )
                        }
                    )
                }
            } else {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Google Sign-In failed: No ID token"
                )
            }
        } catch (e: ApiException) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Google Sign-In failed: ${e.statusCode}"
            )
        }
    }

    private fun validateFields(): Boolean {
        val currentState = _uiState.value

        val isEmailValid = isValidEmail(currentState.email)
        val isPasswordValid = currentState.password.isNotEmpty()

        _uiState.value = currentState.copy(
            isEmailError = !isEmailValid,
            isPasswordError = !isPasswordValid,
            errorMessage = if (!isEmailValid || !isPasswordValid) {
                "Please enter valid credentials"
            } else null
        )

        return isEmailValid && isPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun resetPassword() {
        val currentState = _uiState.value

        if (!isValidEmail(currentState.email)) {
            _uiState.value = currentState.copy(
                isEmailError = true,
                errorMessage = "Please enter a valid email to reset password"
            )
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            // Using sendEmailVerificationCode as a replacement for resetPassword
            val result = authRepository.sendEmailVerificationCode(currentState.email)

            result.fold(
                onSuccess = { message ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                    // Show success message (handled by UI)
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to reset password"
                    )
                }
            )
        }
    }
}