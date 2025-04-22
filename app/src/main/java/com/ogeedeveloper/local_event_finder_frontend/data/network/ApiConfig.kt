package com.ogeedeveloper.local_event_finder_frontend.data.network

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Configuration class for API endpoints
 * Handles different environments (development and production)
 */
@Singleton
class ApiConfig @Inject constructor() {
    companion object {
        // Set to true for development environment, false for production
        private const val IS_DEV_ENVIRONMENT = true
        
        // Development base URL (local development server)
        private const val DEV_BASE_URL = "http://localhost:8000/api/"
        
        // Production base URL (replace with your actual production API URL)
        private const val PROD_BASE_URL = "https://api.yourdomain.com/api/"
        
        // API endpoints (relative paths)
        const val LOGIN_ENDPOINT = "auth/login"
        const val REGISTER_ENDPOINT = "auth/register"
        const val SEND_PHONE_CODE_ENDPOINT = "auth/send-phone-code"
        const val VERIFY_PHONE_ENDPOINT = "auth/verify-phone"
        const val EVENTS_ENDPOINT = "events"
        const val USER_PROFILE_ENDPOINT = "users/profile"
    }
    
    /**
     * Get the base URL based on the current environment
     */
    fun getBaseUrl(): String {
        return if (IS_DEV_ENVIRONMENT) DEV_BASE_URL else PROD_BASE_URL
    }
    
    /**
     * Get the full URL for a specific endpoint
     */
    fun getFullUrl(endpoint: String): String {
        return getBaseUrl() + endpoint
    }
    
    /**
     * Get login URL
     */
    fun getLoginUrl(): String = getFullUrl(LOGIN_ENDPOINT)
    
    /**
     * Get register URL
     */
    fun getRegisterUrl(): String = getFullUrl(REGISTER_ENDPOINT)
    
    /**
     * Get send phone code URL
     */
    fun getSendPhoneCodeUrl(): String = getFullUrl(SEND_PHONE_CODE_ENDPOINT)
    
    /**
     * Get verify phone URL
     */
    fun getVerifyPhoneUrl(): String = getFullUrl(VERIFY_PHONE_ENDPOINT)
    
    /**
     * Get events URL
     */
    fun getEventsUrl(): String = getFullUrl(EVENTS_ENDPOINT)
    
    /**
     * Get specific event URL by ID
     */
    fun getEventByIdUrl(eventId: String): String = getFullUrl("$EVENTS_ENDPOINT/$eventId")
    
    /**
     * Get user profile URL
     */
    fun getUserProfileUrl(): String = getFullUrl(USER_PROFILE_ENDPOINT)
}
