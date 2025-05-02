package com.ogeedeveloper.local_event_finder_frontend.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Location
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Custom deserializer for Event objects to properly handle location data from the API
 */
class EventDeserializer : JsonDeserializer<Event> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Event {
        val jsonObject = json.asJsonObject

        // Extract basic event properties
        val id = jsonObject.get("id")?.asString ?: ""
        val title = jsonObject.get("title")?.asString ?: ""
        val description = jsonObject.get("description")?.asString ?: ""
        val category = jsonObject.get("category")?.asString ?: ""
        val price = jsonObject.get("price")?.asDouble ?: 0.0
        val coverImage = jsonObject.get("cover_image")?.asString
        val organizerUid = jsonObject.get("organizer_uid")?.asString ?: ""

        // Parse dates
        val startDate = parseDate(jsonObject, "date_time")
        val endDate = parseDate(jsonObject, "end_time")

        // Extract location data
        val location = extractLocation(jsonObject)

        return Event(
            id = id,
            title = title,
            description = description,
            category = category,
            price = price,
            imageUrl = coverImage,
            organizer = organizerUid,
            startDate = startDate ?: Date(),
            endDate = endDate,
            location = location
        )
    }

    private fun parseDate(jsonObject: JsonObject, fieldName: String): Date? {
        return try {
            val dateStr = jsonObject.get(fieldName)?.asString
            if (dateStr.isNullOrEmpty()) null else dateFormat.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    private fun extractLocation(jsonObject: JsonObject): Location? {
        // Check if longitude and latitude are present
        val hasLongitude = jsonObject.has("longitude") && !jsonObject.get("longitude").isJsonNull
        val hasLatitude = jsonObject.has("latitude") && !jsonObject.get("latitude").isJsonNull
        val locationName = jsonObject.get("location_name")?.asString

        return if (hasLongitude && hasLatitude && !locationName.isNullOrEmpty()) {
            val longitude = jsonObject.get("longitude").asDouble
            val latitude = jsonObject.get("latitude").asDouble
            
            Location(
                name = locationName,
                address = locationName,
                latitude = latitude,
                longitude = longitude
            )
        } else {
            null
        }
    }
}
