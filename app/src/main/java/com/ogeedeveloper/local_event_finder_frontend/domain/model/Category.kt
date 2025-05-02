package com.ogeedeveloper.local_event_finder_frontend.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a category of events
 */
data class Category(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("name")
    val name: String = "",
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("iconUrl")
    val iconUrl: String? = null
)