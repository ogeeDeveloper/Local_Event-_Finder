package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.content.Intent
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ogeedeveloper.local_event_finder_frontend.data.network.AuthApi
import com.ogeedeveloper.local_event_finder_frontend.data.storage.AuthLocalDataSource
import com.ogeedeveloper.local_event_finder_frontend.domain.model.AuthSession
import com.ogeedeveloper.local_event_finder_frontend.domain.model.AuthState
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthRepository that handles authentication operations
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authLocalDataSource: AuthLocalDataSource,
    private val sharedPreferences: SharedPreferences,
    private val googleSignInClient: GoogleSignInClient
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    private val _authState = MutableStateFlow(AuthState.LOADING)

    init {
        // Load user from local storage on init
        val savedUser = authLocalDataSource.getUser()
        if (savedUser != null) {
            _currentUser.value = savedUser
            _authState.value = AuthState.AUTHENTICATED
        } else {
            _authState.value = AuthState.UNAUTHENTICATED
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            // Call the API to login
            val response = authApi.login(email, password)
            
            // Extract user and token from response
            val user = response.user
            val token = response.token
            
            // Save user to local state
            _currentUser.value = user
            _authState.value = AuthState.AUTHENTICATED
            
            // Save to local storage
            authLocalDataSource.saveUser(user)
            
            // Create and save auth session with token
            val authSession = AuthSession(
                userId = user.id,
                accessToken = token,
                refreshToken = "", // Not provided in the response
                expiresAt = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000) // Assume 24 hours expiry
            )
            authLocalDataSource.saveAuthSession(authSession)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Result<User> {
        return try {
            // Call the API to register
            val loginResponse = authApi.register(
                name = fullName,
                email = email,
                password = password,
                phoneNumber = phoneNumber
            )
            
            // Extract user and token from response
            val user = loginResponse.user
            val authToken = loginResponse.token
            
            // Save auth session with token
            authLocalDataSource.saveAuthSession(
                AuthSession(
                    accessToken = authToken,
                    expiresAt = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000) // 24 hours from now
                )
            )
            
            // Save user to local storage
            authLocalDataSource.saveUser(user)
            
            // Update current user state
            _currentUser.value = user
            _authState.value = AuthState.AUTHENTICATED
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        _currentUser.value = null
        _authState.value = AuthState.UNAUTHENTICATED

        // Clear local storage
        authLocalDataSource.clearUser()
        authLocalDataSource.clearAuthSession()
    }

    override fun getAuthState(): Flow<AuthState> {
        return _authState
    }

    override fun getCurrentUser(): Flow<User?> {
        return _currentUser
    }

    override suspend fun sendPhoneVerificationCode(phoneNumber: String, userId: String): Result<String> {
        return try {
            // Call the API to send verification code with explicit userId
            val response = authApi.sendPhoneVerificationCode(
                phoneNumber = phoneNumber,
                uid = userId
            )
            
            // For development purposes, return the code in the success message
            // In production, this would just return a success message
            Result.success("Verification code sent successfully. Code: ${response.code}")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPhoneNumber(phoneNumber: String, code: String): Result<String> {
        return try {
            // Call the API to verify phone
            val response = authApi.verifyPhone(
                phoneNumber = phoneNumber,
                code = code
            )

            // Update the current user with verified phone
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(isPhoneVerified = true)
                _currentUser.value = updatedUser
                authLocalDataSource.saveUser(updatedUser)
            }

            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        } as Result<String>
    }

    override suspend fun sendEmailVerificationCode(email: String): Result<String> {
        return try {
            // Mock sending email verification
            delay(1000)
            // Generate a verification code (in a real app, this would come from the server)
            val code = "654321"
            // Store verification code in preferences (for demo only, not secure)
            sharedPreferences.edit().putString("EMAIL_VERIFICATION_CODE", code).apply()
            Result.success("Verification code sent to your email")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyEmail(email: String, code: String): Result<String> {
        return try {
            // Check if code matches stored code
            val storedCode = sharedPreferences.getString("EMAIL_VERIFICATION_CODE", null)
            if (code == storedCode || code == "654321") { // Allow test code for debugging
                val pendingUser = authLocalDataSource.getPendingUser()
                pendingUser?.let {
                    val updatedUser = it.copy(isEmailVerified = true)
                    authLocalDataSource.savePendingUser(updatedUser)
                }
                Result.success("Email verified successfully")
            } else {
                Result.failure(Exception("Invalid verification code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            // In a real implementation, you would call an API to update the user profile
            // For now, we'll just update the local user
            _currentUser.value = user
            authLocalDataSource.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<String> {
        return try {
            val response = authApi.requestPasswordReset(email)
            // Return the verification code if available (for testing) or the success message
            val resultMessage = response.code ?: response.message
            Result.success(resultMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyResetCode(email: String, code: String): Result<String> {
        return try {
            val response = authApi.verifyResetCode(email, code)
            // Return the reset token
            Result.success(response.reset_token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String, resetToken: String, newPassword: String): Result<Boolean> {
        return try {
            val success = authApi.resetPassword(email, resetToken, newPassword)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resendResetCode(email: String): Result<String> {
        return try {
            val response = authApi.resendResetCode(email)
            // Return the verification code if available (for testing) or the success message
            val resultMessage = response.code ?: response.message
            Result.success(resultMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            // Since we don't have Firebase Auth set up yet, we'll directly call the backend API
            val response = authApi.loginWithGoogle(
                idToken = idToken,
                email = "",  // These will be extract
                // ed from the token on the backend
                name = ""
            )
            
            // Create local user from response
            val user = User(
                id = response.user.id,
                fullName = response.user.fullName,
                email = response.user.email,
                phoneNumber = response.user.phoneNumber,
                profileImageUrl = response.user.profileImageUrl
            )
            
            // Save user and token locally
            authLocalDataSource.saveUser(user)
            authLocalDataSource.saveAuthSession(AuthSession(
                userId = user.id,
                accessToken = response.token,
                refreshToken = "", 
                expiresAt = Date(System.currentTimeMillis() + 3600000)
            ))
            
            // Update state
            _currentUser.value = user
            _authState.value = AuthState.AUTHENTICATED
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}