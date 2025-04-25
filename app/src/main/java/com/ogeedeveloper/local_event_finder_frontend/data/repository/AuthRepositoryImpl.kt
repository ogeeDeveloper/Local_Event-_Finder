package com.ogeedeveloper.local_event_finder_frontend.data.repository

import android.content.SharedPreferences
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

    override suspend fun sendPhoneVerificationCode(phoneNumber: String): Result<String> {
        return try {
            // Mock sending SMS verification
            delay(1000)
            // Generate a verification code (in a real app, this would come from the server)
            val code = "123456"
            // Store verification code in preferences (for demo only, not secure)
            sharedPreferences.edit().putString("VERIFICATION_CODE", code).apply()
            Result.success("Verification code sent successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPhoneNumber(phoneNumber: String, code: String): Result<String> {
        return try {
            // Check if code matches stored code
            val storedCode = sharedPreferences.getString("VERIFICATION_CODE", null)
            if (code == storedCode || code == "123456") { // Allow test code for debugging
                val pendingUser = authLocalDataSource.getPendingUser()
                pendingUser?.let {
                    val updatedUser = it.copy(isPhoneVerified = true)
                    authLocalDataSource.savePendingUser(updatedUser)
                }
                Result.success("Phone number verified successfully")
            } else {
                Result.failure(Exception("Invalid verification code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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
            // Update user in local storage
            authLocalDataSource.saveUser(user)
            // Update current user state
            _currentUser.value = user
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}