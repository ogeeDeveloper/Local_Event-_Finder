package com.ogeedeveloper.local_event_finder_frontend.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * A reusable Google Maps component that can be used across the app
 */
@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    title: String,
    modifier: Modifier = Modifier,
    zoomLevel: Float = 14f,
    showCard: Boolean = true,
    onMapClick: (() -> Unit)? = null
) {
    val location = remember { LatLng(latitude, longitude) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoomLevel)
    }
    
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false
        )
    }
    
    val mapProperties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = false
        )
    }
    
    if (showCard) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            MapContent(
                location = location,
                title = title,
                cameraPositionState = cameraPositionState,
                mapUiSettings = mapUiSettings,
                mapProperties = mapProperties,
                onMapClick = onMapClick
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            MapContent(
                location = location,
                title = title,
                cameraPositionState = cameraPositionState,
                mapUiSettings = mapUiSettings,
                mapProperties = mapProperties,
                onMapClick = onMapClick
            )
        }
    }
}

@Composable
private fun MapContent(
    location: LatLng,
    title: String,
    cameraPositionState: com.google.maps.android.compose.CameraPositionState,
    mapUiSettings: MapUiSettings,
    mapProperties: MapProperties,
    onMapClick: (() -> Unit)? = null
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties,
        onMapClick = { onMapClick?.invoke() }
    ) {
        Marker(
            state = MarkerState(position = location),
            title = title,
            snippet = "Location",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
    }
}
