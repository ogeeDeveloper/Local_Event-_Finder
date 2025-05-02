package com.ogeedeveloper.local_event_finder_frontend.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.ogeedeveloper.local_event_finder_frontend.domain.model.AuthSession
import com.ogeedeveloper.local_event_finder_frontend.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

/**
 * Local data source for authentication data
 */
@Singleton
class AuthLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val KEY_USER = "user"
        private const val KEY_PENDING_USER = "pending_user"
        private const val KEY_AUTH_SESSION = "auth_session"
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit { putString(KEY_USER, userJson) }
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    fun clearUser() {
        sharedPreferences.edit { remove(KEY_USER) }
    }

    fun savePendingUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit { putString(KEY_PENDING_USER, userJson) }
    }

    fun getPendingUser(): User? {
        val userJson = sharedPreferences.getString(KEY_PENDING_USER, null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    fun clearPendingUser() {
        sharedPreferences.edit { remove(KEY_PENDING_USER) }
    }

    fun saveAuthSession(session: AuthSession) {
        val sessionJson = gson.toJson(session)
        sharedPreferences.edit { putString(KEY_AUTH_SESSION, sessionJson) }
    }

    fun getAuthSession(): AuthSession? {
        val sessionJson = sharedPreferences.getString(KEY_AUTH_SESSION, null)
        return sessionJson?.let { gson.fromJson(it, AuthSession::class.java) }
    }

    fun clearAuthSession() {
        sharedPreferences.edit { remove(KEY_AUTH_SESSION) }
    }
    
    /**
     * Get the authentication token from the stored auth session
     * @return The access token or null if no session exists
     */
    fun getAuthToken(): String? {
        return getAuthSession()?.accessToken
    }
}