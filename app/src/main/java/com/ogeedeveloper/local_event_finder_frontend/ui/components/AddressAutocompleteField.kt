package com.ogeedeveloper.local_event_finder_frontend.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.ogeedeveloper.local_event_finder_frontend.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Address autocomplete field that uses Google Places API to suggest addresses
 * with a focus on Jamaica addresses
 */
@Composable
fun AddressAutocompleteField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddressSelected: (String, Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Enter address"
) {
    val context = LocalContext.current
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }
    var placesClient: PlacesClient? = null
    
    // Initialize Places API if not already initialized
    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            // Use the API key from BuildConfig instead of strings.xml
            Places.initialize(context, BuildConfig.MAPS_API_KEY)
        }
        placesClient = Places.createClient(context)
    }
    
    // Fetch predictions when value changes
    LaunchedEffect(value) {
        if (value.length >= 3) {
            predictions = fetchPredictions(context, value, placesClient)
            showSuggestions = predictions.isNotEmpty()
        } else {
            predictions = emptyList()
            showSuggestions = false
        }
    }
    
    Column(modifier = modifier) {
        // Regular text field for address input
        AppTextField(
            value = value,
            onValueChange = { 
                onValueChange(it)
                showSuggestions = it.length >= 3
            },
            placeholder = placeholder,
            leadingIcon = Icons.Default.LocationOn,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Suggestions dropdown
        if (showSuggestions) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(predictions) { prediction ->
                        TextButton(
                            onClick = {
                                // When a suggestion is clicked, fetch the place details
                                fetchPlaceDetails(
                                    context = context,
                                    placeId = prediction.placeId,
                                    placesClient = placesClient,
                                    onPlaceSelected = { address, lat, lng ->
                                        onValueChange(address)
                                        onAddressSelected(address, lat, lng)
                                        showSuggestions = false
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = prediction.getPrimaryText(null).toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = prediction.getSecondaryText(null).toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Fetch address predictions from Google Places API
 */
private suspend fun fetchPredictions(
    context: Context,
    query: String,
    placesClient: PlacesClient?
): List<AutocompletePrediction> = withContext(Dispatchers.IO) {
    if (placesClient == null || query.length < 3) return@withContext emptyList()
    
    try {
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountries("JM")  // Restrict to Jamaica
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(query)
            .build()
            
        val response = placesClient.findAutocompletePredictions(request).await()
        return@withContext response.autocompletePredictions
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext emptyList()
    }
}

/**
 * Fetch place details from Google Places API
 */
private fun fetchPlaceDetails(
    context: Context,
    placeId: String,
    placesClient: PlacesClient?,
    onPlaceSelected: (String, Double, Double) -> Unit
) {
    if (placesClient == null) return
    
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)
    
    placesClient.fetchPlace(request).addOnSuccessListener { response ->
        val place = response.place
        val address = place.address ?: ""
        val latLng = place.latLng
        
        if (latLng != null) {
            onPlaceSelected(address, latLng.latitude, latLng.longitude)
        }
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
}
