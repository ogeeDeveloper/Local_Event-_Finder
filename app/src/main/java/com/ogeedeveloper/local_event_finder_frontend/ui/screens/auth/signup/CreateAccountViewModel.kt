package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * ViewModel for the Create Account screen
 */
@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAccountUiState())
    val uiState: StateFlow<CreateAccountUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(fullName: String) {
        _uiState.value = _uiState.value.copy(
            fullName = fullName,
            isFullNameError = fullName.isEmpty()
        )
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            isEmailError = !isValidEmail(email),
            emailErrorMessage = if (!isValidEmail(email) && email.isNotEmpty()) {
                "Please enter a valid email address"
            } else null
        )
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phoneNumber,
            isPhoneError = phoneNumber.isEmpty()
        )
    }

    fun onCountryCodeChanged(countryCode: String) {
        _uiState.value = _uiState.value.copy(
            countryCode = countryCode
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isPasswordError = !isValidPassword(password) && password.isNotEmpty(),
            passwordErrorMessage = getPasswordErrorMessage(password)
        )
        validateConfirmPassword()
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            isConfirmPasswordError = confirmPassword != _uiState.value.password && confirmPassword.isNotEmpty()
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible
        )
    }

    // Legacy methods - kept for backward compatibility
    fun updateFullName(fullName: String) {
        onFullNameChanged(fullName)
    }

    fun updateEmail(email: String) {
        onEmailChanged(email)
    }

    fun updatePassword(password: String) {
        onPasswordChanged(password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        onConfirmPasswordChanged(confirmPassword)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        // Extract country code if present
        if (phoneNumber.startsWith("+")) {
            val countryCode = phoneNumber.takeWhile { it != ' ' && it.toString() != phoneNumber.last().toString() }
            val number = phoneNumber.substringAfter(countryCode)
            onCountryCodeChanged(countryCode)
            onPhoneNumberChanged(number)
        } else {
            onPhoneNumberChanged(phoneNumber)
        }
    }

    fun createAccount() {
        val currentState = _uiState.value

        // Validate all fields
        if (!validateFields()) {
            return
        }

        // Update loading state
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        // Attempt to create account
        viewModelScope.launch {
            val result = authRepository.signUp(
                fullName = currentState.fullName,
                email = currentState.email,
                phoneNumber = formatPhoneNumber(currentState.countryCode, currentState.phoneNumber),
                password = currentState.password
            )

            result.fold(
                onSuccess = { user ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        successMessage = "Account created successfully",
                        userId = user.uid
                    )
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to create account"
                    )
                }
            )
        }
    }

    private fun validateConfirmPassword() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isConfirmPasswordError = currentState.confirmPassword.isNotEmpty() &&
                    currentState.confirmPassword != currentState.password
        )
    }

    private fun validateFields(): Boolean {
        val currentState = _uiState.value

        val isFullNameValid = currentState.fullName.isNotEmpty()
        val isEmailValid = isValidEmail(currentState.email)
        val isPhoneValid = currentState.phoneNumber.isNotEmpty()
        val isPasswordValid = isValidPassword(currentState.password)
        val isConfirmPasswordValid = currentState.confirmPassword == currentState.password

        _uiState.value = currentState.copy(
            isFullNameError = !isFullNameValid,
            isEmailError = !isEmailValid,
            isPhoneError = !isPhoneValid,
            isPasswordError = !isPasswordValid,
            isConfirmPasswordError = !isConfirmPasswordValid
        )

        return isFullNameValid && isEmailValid && isPhoneValid &&
                isPasswordValid && isConfirmPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun getPasswordErrorMessage(password: String): String? {
        return if (password.isEmpty()) {
            null
        } else if (password.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }

    private fun formatPhoneNumber(countryCode: String, phoneNumber: String): String {
        // Remove any non-digit characters from the phone number
        val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")
        
        // Ensure country code starts with +
        val formattedCountryCode = if (countryCode.startsWith("+")) countryCode else "+$countryCode"
        
        // Return the formatted phone number in E.164 format
        return "$formattedCountryCode$digitsOnly"
    }
}
