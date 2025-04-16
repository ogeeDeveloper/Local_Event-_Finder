package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock API interfaces for illustration purposes
 */
@Singleton
class AuthApi @Inject constructor() {
    suspend fun login(email: String, password: String): User {
        // In a real app, this would make an API call
        throw NotImplementedError("This would be implemented with Retrofit")
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        phoneNumber: String
    ): User {
        throw NotImplementedError("This would be implemented with Retrofit")
    }
}