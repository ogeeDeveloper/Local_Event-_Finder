package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API interface for user-related operations
 * Uses ApiConfig to determine the correct endpoints for different environments
 */
@Singleton
class UserApi @Inject constructor(private val apiConfig: ApiConfig) {
    suspend fun getProfile(userId: String): User {
        // In a real app, this would make an API call to apiConfig.getUserProfileUrl()
        // Example: retrofit.get(apiConfig.getUserProfileUrl())
        throw NotImplementedError("This would be implemented with Retrofit calling ${apiConfig.getUserProfileUrl()}")
    }

    suspend fun updateProfile(user: User): User {
        // In a real app, this would make an API call to apiConfig.getUserProfileUrl()
        // Example: retrofit.put(apiConfig.getUserProfileUrl(), user)
        throw NotImplementedError("This would be implemented with Retrofit calling ${apiConfig.getUserProfileUrl()}")
    }
}