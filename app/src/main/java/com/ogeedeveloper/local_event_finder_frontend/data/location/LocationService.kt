package com.ogeedeveloper.local_event_finder_frontend.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for handling location-related operations like geocoding
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Convert an address string to latitude and longitude coordinates
     * @param address The address to geocode
     * @return A Pair of (latitude, longitude) or null if geocoding failed
     */
    suspend fun geocodeAddress(address: String): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        try {
            if (address.isBlank()) {
                return@withContext null
            }
            
            val geocoder = Geocoder(context, Locale.getDefault())
            
            // Use getFromLocationName for geocoding
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(address, 1)
            
            if (addresses.isNullOrEmpty()) {
                return@withContext null
            }
            
            val location = addresses[0]
            return@withContext Pair(location.latitude, location.longitude)
        } catch (e: IOException) {
            e.printStackTrace()
            return@withContext null
        }
    }
    
    /**
     * Create a properly formatted location string for MySQL Spatial data with SRID 4326
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @return A properly formatted location string or null if coordinates are invalid
     */
    fun createSpatialPoint(latitude: Double, longitude: Double): String {
        // Format for MySQL: POINT(longitude latitude)
        // Note: MySQL expects longitude first, then latitude
        return "POINT($longitude $latitude)"
    }
    
    /**
     * Check if coordinates are valid
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @return True if coordinates are valid, false otherwise
     */
    fun areCoordinatesValid(latitude: Double, longitude: Double): Boolean {
        return latitude != 0.0 || longitude != 0.0
    }
}
