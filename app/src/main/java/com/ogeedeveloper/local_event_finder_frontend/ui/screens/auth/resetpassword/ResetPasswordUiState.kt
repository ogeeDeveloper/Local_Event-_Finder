package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.resetpassword

/**
 * Represents the UI state for the reset password flow
 */
data class ResetPasswordUiState(
    val email: String = "",
    val verificationCode: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val resetToken: String = "",
    val isLoading: Boolean = false,
    val isEmailSubmitted: Boolean = false,
    val isCodeVerified: Boolean = false,
    val isPasswordReset: Boolean = false,
    val errorMessage: String? = null
)
