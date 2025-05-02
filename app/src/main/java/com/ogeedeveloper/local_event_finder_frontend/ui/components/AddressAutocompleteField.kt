package com.ogeedeveloper.local_event_finder_frontend.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    var placesClient by remember { mutableStateOf<PlacesClient?>(null) }
    
    // Initialize Places API if not already initialized
    LaunchedEffect(Unit) {
        try {
            if (!Places.isInitialized()) {
                // Use the API key from BuildConfig
                Places.initialize(context, BuildConfig.MAPS_API_KEY)
                android.util.Log.d("AddressAutocomplete", "Places API initialized with key")
            }
            placesClient = Places.createClient(context)
        } catch (e: Exception) {
            android.util.Log.e("AddressAutocomplete", "Error initializing Places API: ${e.message}")
            e.printStackTrace()
        }
    }
    
    // Fetch predictions when value changes
    LaunchedEffect(value) {
        try {
            if (value.length >= 3 && placesClient != null) {
                // Create a new token for each session to improve results
                val token = AutocompleteSessionToken.newInstance()
                
                val request = FindAutocompletePredictionsRequest.builder()
                    .setCountries("JM")  // Restrict to Jamaica
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(value)
                    .build()
                
                withContext(Dispatchers.IO) {
                    try {
                        val response = placesClient!!.findAutocompletePredictions(request).await()
                        val newPredictions = response.autocompletePredictions
                        
                        withContext(Dispatchers.Main) {
                            predictions = newPredictions
                            showSuggestions = newPredictions.isNotEmpty()
                            android.util.Log.d("AddressAutocomplete", "Fetched ${newPredictions.size} predictions for query: $value")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AddressAutocomplete", "Error in API call: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } else {
                predictions = emptyList()
                showSuggestions = false
            }
        } catch (e: Exception) {
            android.util.Log.e("AddressAutocomplete", "Error fetching predictions: ${e.message}")
            e.printStackTrace()
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
        if (showSuggestions && predictions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)  
                ) {
                    items(predictions) { prediction ->
                        TextButton(
                            onClick = {
                                val primaryText = prediction.getPrimaryText(null).toString()
                                val secondaryText = prediction.getSecondaryText(null).toString()
                                val fullAddress = "$primaryText, $secondaryText"
                                
                                android.util.Log.d("AddressAutocomplete", "Clicked on prediction: $fullAddress")
                                
                                // Set the value immediately for better UX
                                onValueChange(fullAddress)
                                
                                // Launch a coroutine to fetch place details
                                kotlinx.coroutines.MainScope().launch {
                                    try {
                                        // Get place details
                                        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
                                        val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)
                                        
                                        val response = withContext(Dispatchers.IO) {
                                            placesClient?.fetchPlace(request)?.await()
                                        }
                                        
                                        if (response != null) {
                                            val place = response.place
                                            val address = place.address ?: fullAddress
                                            val latLng = place.latLng
                                            
                                            if (latLng != null) {
                                                android.util.Log.d("AddressAutocomplete", "Selected address: $address at ${latLng.latitude}, ${latLng.longitude}")
                                                onValueChange(address)
                                                onAddressSelected(address, latLng.latitude, latLng.longitude)
                                            }
                                        }
                                        
                                        // Hide suggestions after selection
                                        showSuggestions = false
                                    } catch (e: Exception) {
                                        android.util.Log.e("AddressAutocomplete", "Error selecting address: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
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
                                    style = MaterialTheme.typography.bodyMedium,
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
 * A simple text field for address input
 */
@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        },
        modifier = modifier,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        singleLine = true
    )
}
