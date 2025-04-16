package com.ogeedeveloper.local_event_finder_frontend.data.repository

import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserRepository
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userPreferences: UserPreferences
) : UserRepository {

    override suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            // Mock API call
            delay(1000)
            userPreferences.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserInterests(interests: List<String>): Result<Boolean> {
        return try {
            // Get current user
            val currentUser = userPreferences.getUser()

            // Update interests
            currentUser?.let {
                val updatedUser = it.copy(interests = interests)
                userPreferences.saveUser(updatedUser)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserInterests(): Flow<List<String>> {
        return userPreferences.getUserFlow().map { it?.interests ?: emptyList() }
    }

    override suspend fun enableNotificationTypes(types: List<String>): Result<Boolean> {
        return try {
            val currentUser = userPreferences.getUser()

            currentUser?.let {
                val notificationPrefs = NotificationPreferences(
                    newEvents = types.contains("new_events"),
                    priceDrops = types.contains("price_drops"),
                    friendEvents = types.contains("friend_events"),
                    eventReminders = types.contains("reminders")
                )

                val updatedUser = it.copy(notificationPreferences = notificationPrefs)
                userPreferences.saveUser(updatedUser)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNotificationTypes(): Flow<List<String>> {
        return userPreferences.getUserFlow().map { user ->
            val prefs = user?.notificationPreferences
            val types = mutableListOf<String>()

            prefs?.let {
                if (it.newEvents) types.add("new_events")
                if (it.priceDrops) types.add("price_drops")
                if (it.friendEvents) types.add("friend_events")
                if (it.eventReminders) types.add("reminders")
            }

            types
        }
    }

    override suspend fun updateLocationPreference(useLocation: Boolean): Result<Boolean> {
        return try {
            val currentUser = userPreferences.getUser()

            currentUser?.let {
                val updatedLocationPrefs = it.locationPreferences.copy(
                    useLocationServices = useLocation
                )

                val updatedUser = it.copy(locationPreferences = updatedLocationPrefs)
                userPreferences.saveUser(updatedUser)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLocationPreference(): Flow<Boolean> {
        return userPreferences.getUserFlow().map {
            it?.locationPreferences?.useLocationServices ?: false
        }
    }
}