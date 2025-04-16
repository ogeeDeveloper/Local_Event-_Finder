package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.content.SharedPreferences
import com.ogeedeveloper.local_event_finder_frontend.domain.model.AuthState
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthRepository with mock data (would be replaced with real API calls)
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authLocalDataSource: AuthLocalDataSource,
    private val sharedPreferences: SharedPreferences
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
            // In a real app, this would call the API
            delay(1000) // Simulate network delay

            // Mock successful login
            if (email.contains("@") && password.length >= 6) {
                val user = User(
                    id = UUID.randomUUID().toString(),
                    fullName = "John Doe",
                    email = email,
                    isEmailVerified = true,
                    phoneNumber = "+1234567890",
                    isPhoneVerified = true
                )
                _currentUser.value = user
                _authState.value = AuthState.AUTHENTICATED

                // Save to local storage
                authLocalDataSource.saveUser(user)
                authLocalDataSource.saveAuthSession(
                    AuthSession(
                        userId = user.id,
                        accessToken = "mock_token_${user.id}",
                        refreshToken = "mock_refresh_${user.id}",
                        expiresAt = Date(System.currentTimeMillis() + 3600000)
                    )
                )

                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
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
            // Mock signup process
            delay(1500) // Simulate network delay

            val user = User(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                isEmailVerified = false,
                isPhoneVerified = false
            )

            // In a real app, store temporarily until verification
            authLocalDataSource.savePendingUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendEmailVerification(): Result<Boolean> {
        return try {
            // Mock sending email verification
            delay(1000)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPhoneVerification(phoneNumber: String): Result<Boolean> {
        return try {
            // Mock sending SMS verification
            delay(1000)
            // Store verification code in preferences (for demo only, not secure)
            sharedPreferences.edit().putString("VERIFICATION_CODE", "123456").apply()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyEmail(code: String): Result<Boolean> {
        return try {
            // Mock email verification
            delay(1000)

            val pendingUser = authLocalDataSource.getPendingUser()
            pendingUser?.let {
                val updatedUser = it.copy(isEmailVerified = true)
                authLocalDataSource.savePendingUser(updatedUser)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPhone(code: String): Result<Boolean> {
        return try {
            // Check if code matches stored code
            val storedCode = sharedPreferences.getString("VERIFICATION_CODE", null)
            if (code == storedCode || code == "123456") { // Allow test code for debugging
                val pendingUser = authLocalDataSource.getPendingUser()
                pendingUser?.let {
                    val updatedUser = it.copy(isPhoneVerified = true)
                    authLocalDataSource.savePendingUser(updatedUser)
                }
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid verification code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            // Mock password reset
            delay(1000)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            _currentUser.value = null
            _authState.value = AuthState.UNAUTHENTICATED

            // Clear local storage
            authLocalDataSource.clearUser()
            authLocalDataSource.clearAuthSession()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserSignedIn(): Flow<Boolean> {
        return _authState.map { it == AuthState.AUTHENTICATED }
    }

    override fun getCurrentUser(): Flow<User?> {
        return _currentUser.asStateFlow()
    }
}