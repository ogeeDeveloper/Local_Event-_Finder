package com.ogeedeveloper.local_event_finder_frontend.domain.repository

import com.ogeedeveloper.local_event_finder_frontend.domain.model.AuthState
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    /**
     * Sign in a user with email and password
     * @param email User's email
     * @param password User's password
     * @return Result containing the User if successful, or an exception if failed
     */
    suspend fun signIn(email: String, password: String): Result<User>

    /**
     * Sign up a new user
     * @param fullName User's full name
     * @param email User's email
     * @param phoneNumber User's phone number
     * @param password User's password
     * @return Result containing the User if successful, or an exception if failed
     */
    suspend fun signUp(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Result<User>

    /**
     * Sign out the current user
     */
    suspend fun signOut()

    /**
     * Get the current authentication state
     * @return Flow of AuthState
     */
    fun getAuthState(): Flow<AuthState>

    /**
     * Get the current user
     * @return Flow of User or null if not authenticated
     */
    fun getCurrentUser(): Flow<User?>

    /**
     * Send verification code to user's phone
     * @param phoneNumber Phone number to verify
     * @return Result containing success message or failure
     */
    suspend fun sendPhoneVerificationCode(phoneNumber: String): Result<String>

    /**
     * Verify phone number with code
     * @param phoneNumber Phone number being verified
     * @param code Verification code
     * @return Result containing success message or failure
     */
    suspend fun verifyPhoneNumber(phoneNumber: String, code: String): Result<String>

    /**
     * Send verification code to user's email
     * @param email Email to verify
     * @return Result containing success message or failure
     */
    suspend fun sendEmailVerificationCode(email: String): Result<String>

    /**
     * Verify email with code
     * @param email Email being verified
     * @param code Verification code
     * @return Result containing success message or failure
     */
    suspend fun verifyEmail(email: String, code: String): Result<String>

    /**
     * Update user profile
     * @param user Updated user data
     * @return Result containing updated User if successful
     */
    suspend fun updateUserProfile(user: User): Result<User>

    /**
     * Request a password reset for the given email
     * @param email User's email
     * @return Result containing a String with the verification code (if available) or success message
     */
    suspend fun requestPasswordReset(email: String): Result<String>

    /**
     * Verify a password reset code
     * @param email User's email
     * @param code Verification code
     * @return Result containing the reset token if successful
     */
    suspend fun verifyResetCode(email: String, code: String): Result<String>

    /**
     * Reset the password using the reset token
     * @param email User's email
     * @param resetToken Reset token from verify reset code
     * @param newPassword New password
     * @return Result containing a Boolean indicating success or failure
     */
    suspend fun resetPassword(email: String, resetToken: String, newPassword: String): Result<Boolean>

    /**
     * Resend the password reset code
     * @param email User's email
     * @return Result containing a String with the verification code (if available) or success message
     */
    suspend fun resendResetCode(email: String): Result<String>
}
