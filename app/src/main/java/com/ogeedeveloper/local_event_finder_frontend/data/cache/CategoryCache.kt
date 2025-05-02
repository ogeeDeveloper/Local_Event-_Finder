package com.ogeedeveloper.local_event_finder_frontend.data.cache

import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton cache for storing categories to reduce API calls.
 * Uses an in-memory cache with expiration time.
 */
@Singleton
class CategoryCache @Inject constructor() {
    
    private val mutex = Mutex()
    private var categories: List<Category>? = null
    private var lastUpdated: Long = 0
    
    // Cache expiration time in milliseconds (default: 30 minutes)
    private val cacheExpirationTime = TimeUnit.MINUTES.toMillis(30)
    
    /**
     * Get categories from cache if available and not expired
     * @return List of categories or null if cache is empty or expired
     */
    suspend fun getCategories(): List<Category>? = mutex.withLock {
        val currentTime = System.currentTimeMillis()
        if (categories != null && currentTime - lastUpdated < cacheExpirationTime) {
            return categories
        }
        return null
    }
    
    /**
     * Store categories in cache
     * @param newCategories List of categories to store
     */
    suspend fun storeCategories(newCategories: List<Category>) = mutex.withLock {
        categories = newCategories
        lastUpdated = System.currentTimeMillis()
    }
    
    /**
     * Clear the cache
     */
    suspend fun clearCache() = mutex.withLock {
        categories = null
        lastUpdated = 0
    }
    
    /**
     * Check if cache is valid (not null and not expired)
     * @return true if cache is valid, false otherwise
     */
    suspend fun isCacheValid(): Boolean = mutex.withLock {
        val currentTime = System.currentTimeMillis()
        return categories != null && currentTime - lastUpdated < cacheExpirationTime
    }
}
