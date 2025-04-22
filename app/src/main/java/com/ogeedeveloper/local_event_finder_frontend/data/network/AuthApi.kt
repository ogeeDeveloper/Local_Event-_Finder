package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.LoginResponse
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API interface for authentication-related operations
 * Uses ApiConfig to determine the correct endpoints for different environments
 */
@Singleton
class AuthApi @Inject constructor(
    private val authService: AuthService,
    private val apiConfig: ApiConfig
) {
    
    /**
     * Login with email and password
     * @param email User's email
     * @param password User's password
     * @return LoginResponse containing token and user data
     */
    suspend fun login(email: String, password: String): LoginResponse {
        // Make an actual API call to the backend
        val loginRequest = LoginRequest(email = email, password = password)
        val response = authService.login(loginRequest)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Login failed: ${response.code()} ${response.message()}")
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        phoneNumber: String
    ): User {
        val registerRequest = RegisterRequest(
            fullName = name,
            email = email,
            password = password,
            phoneNumber = phoneNumber
        )
        
        val response = authService.register(registerRequest)
        
        if (response.isSuccessful) {
            return response.body()?.user ?: throw Exception("Empty response body")
        } else {
            throw Exception("Registration failed: ${response.code()} ${response.message()}")
        }
    }
    
    suspend fun sendPhoneCode(phoneNumber: String): Boolean {
        val request = SendPhoneCodeRequest(phoneNumber = phoneNumber)
        val response = authService.sendPhoneCode(request)
        
        if (response.isSuccessful) {
            return response.body()?.success == true
        } else {
            throw Exception("Failed to send phone code: ${response.code()} ${response.message()}")
        }
    }
    
    suspend fun verifyPhone(phoneNumber: String, code: String): Boolean {
        val request = VerifyPhoneRequest(phoneNumber = phoneNumber, code = code)
        val response = authService.verifyPhone(request)
        
        if (response.isSuccessful) {
            return response.body()?.success == true
        } else {
            throw Exception("Failed to verify phone: ${response.code()} ${response.message()}")
        }
    }
}

/**
 * Retrofit interface for auth API calls
 */
interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
    
    @POST("auth/send-phone-code")
    suspend fun sendPhoneCode(@Body request: SendPhoneCodeRequest): Response<ApiResponse>
    
    @POST("auth/verify-phone")
    suspend fun verifyPhone(@Body request: VerifyPhoneRequest): Response<ApiResponse>
}

/**
 * Login request data class
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Register request data class
 */
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)

/**
 * Send phone code request data class
 */
data class SendPhoneCodeRequest(
    val phoneNumber: String
)

/**
 * Verify phone request data class
 */
data class VerifyPhoneRequest(
    val phoneNumber: String,
    val code: String
)

/**
 * Generic API response for simple success/failure operations
 */
data class ApiResponse(
    val success: Boolean,
    val message: String? = null
)