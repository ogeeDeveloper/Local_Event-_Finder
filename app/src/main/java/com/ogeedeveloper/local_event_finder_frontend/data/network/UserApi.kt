package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserApi @Inject constructor() {
    suspend fun getProfile(userId: String): User {
        throw NotImplementedError("This would be implemented with Retrofit")
    }

    suspend fun updateProfile(user: User): User {
        throw NotImplementedError("This would be implemented with Retrofit")
    }
}