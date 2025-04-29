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
    ): LoginResponse {
        val registerRequest = RegisterRequest(
            fullName = name,
            email = email,
            password = password,
            phoneNumber = phoneNumber,
            interests = emptyList()
        )
        
        val response = authService.register(registerRequest)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Registration failed: ${response.code()} ${response.message()}")
        }
    }
    
    suspend fun sendPhoneVerificationCode(phoneNumber: String, uid: String): SendPhoneCodeResponse {
        val request = SendPhoneCodeRequest(
            phoneNumber = phoneNumber,
            uid = uid
        )
        
        val response = authService.sendPhoneCode(request)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to send verification code: ${response.code()} ${response.message()}")
        }
    }
    
    suspend fun verifyPhone(phoneNumber: String, code: String): ApiResponse {
        val request = VerifyPhoneRequest(
            phoneNumber = phoneNumber,
            code = code
        )
        
        val response = authService.verifyPhone(request)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Phone verification failed: ${response.code()} ${response.message()}")
        }
    }
    
    /**
     * Request password reset
     * @param email User's email
     * @return RequestResetPasswordResponse containing message, code and email
     */
    suspend fun requestPasswordReset(email: String): RequestResetPasswordResponse {
        val request = RequestResetPasswordRequest(email = email)
        val response = authService.requestResetPassword(request)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to request password reset: ${response.code()} ${response.message()}")
        }
    }
    
    /**
     * Verify reset code
     * @param email User's email
     * @param code Verification code
     * @return VerifyResetCodeResponse containing message, reset_token and email
     */
    suspend fun verifyResetCode(email: String, code: String): VerifyResetCodeResponse {
        val request = VerifyResetCodeRequest(email = email, code = code)
        val response = authService.verifyResetCode(request)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to verify reset code: ${response.code()} ${response.message()}")
        }
    }
    
    /**
     * Reset password
     * @param email User's email
     * @param resetToken Reset token from verify reset code response
     * @param newPassword New password
     * @return True if password was reset successfully
     */
    suspend fun resetPassword(email: String, resetToken: String, newPassword: String): Boolean {
        val request = ResetPasswordRequest(
            email = email,
            reset_token = resetToken,
            password = newPassword
        )
        val response = authService.resetPassword(request)
        
        if (response.isSuccessful) {
            return true
        } else {
            throw Exception("Failed to reset password: ${response.code()} ${response.message()}")
        }
    }
    
    /**
     * Resend reset code
     * @param email User's email
     * @return RequestResetPasswordResponse containing message, code and email
     */
    suspend fun resendResetCode(email: String): RequestResetPasswordResponse {
        val request = ResendResetCodeRequest(email = email)
        val response = authService.resendResetCode(request)
        
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to resend reset code: ${response.code()} ${response.message()}")
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
    suspend fun sendPhoneCode(@Body request: SendPhoneCodeRequest): Response<SendPhoneCodeResponse>
    
    @POST("auth/verify-phone")
    suspend fun verifyPhone(@Body request: VerifyPhoneRequest): Response<ApiResponse>
    
    @POST("auth/request-reset")
    suspend fun requestResetPassword(@Body request: RequestResetPasswordRequest): Response<RequestResetPasswordResponse>
    
    @POST("auth/verify-reset-code")
    suspend fun verifyResetCode(@Body request: VerifyResetCodeRequest): Response<VerifyResetCodeResponse>
    
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
    
    @POST("auth/resend-reset-code")
    suspend fun resendResetCode(@Body request: ResendResetCodeRequest): Response<RequestResetPasswordResponse>
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
    val phoneNumber: String,
    val interests: List<String>
)

/**
 * Send phone code request data class
 */
data class SendPhoneCodeRequest(
    val phoneNumber: String,
    val uid: String
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

/**
 * Request reset password request data class
 */
data class RequestResetPasswordRequest(
    val email: String
)

/**
 * Request reset password response data class
 */
data class RequestResetPasswordResponse(
    val message: String,
    val code: String? = null,
    val email: String
)

/**
 * Verify reset code request data class
 */
data class VerifyResetCodeRequest(
    val email: String,
    val code: String
)

/**
 * Verify reset code response data class
 */
data class VerifyResetCodeResponse(
    val message: String,
    val reset_token: String,
    val email: String
)

/**
 * Reset password request data class
 */
data class ResetPasswordRequest(
    val email: String,
    val reset_token: String,
    val password: String
)

/**
 * Resend reset code request data class
 */
data class ResendResetCodeRequest(
    val email: String
)

/**
 * Send phone code response data class
 */
data class SendPhoneCodeResponse(
    val message: String,
    val code: String? = null,
    val phoneNumber: String
)