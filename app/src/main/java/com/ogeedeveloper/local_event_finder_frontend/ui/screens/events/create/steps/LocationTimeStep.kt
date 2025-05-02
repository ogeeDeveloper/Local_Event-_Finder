package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AddressAutocompleteField
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationTimeStep(
    location: String,
    onLocationChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    startTime: String,
    onStartTimeChange: (String) -> Unit,
    endTime: String,
    onEndTimeChange: (String) -> Unit,
    onCoordinatesChange: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("MM/dd/yyyy", Locale.US) }
    
    // State for time pickers
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.US) }
    
    // Location coordinates state
    var coordinates by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Event Location
        Text(
            text = "Event Location",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        AddressAutocompleteField(
            value = location,
            onValueChange = onLocationChange,
            onAddressSelected = { address, lat, lng ->
                onLocationChange(address)
                onCoordinatesChange(lat, lng)
                coordinates = Pair(lat, lng)
            },
            placeholder = "Enter location in Jamaica",
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Map Preview
        MapPreviewBox(coordinates = coordinates)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Date
        Text(
            text = "Event Date",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = date,
            onValueChange = { /* Handled by date picker */ },
            placeholder = { Text("mm/dd/yyyy") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_picker),
                    contentDescription = "Pick date",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            readOnly = true
        )
        
        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Date(millis)
                                onDateChange(dateFormatter.format(selectedDate))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Time
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Start Time
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Start Time",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { /* Handled by time picker */ },
                    placeholder = { Text("--:-- --") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_time_picker),
                            contentDescription = "Pick time",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { showStartTimePicker = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartTimePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    readOnly = true
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // End Time
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "End Time",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { /* Handled by time picker */ },
                    placeholder = { Text("--:-- --") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_time_picker),
                            contentDescription = "Pick end time",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { showEndTimePicker = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndTimePicker = true },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    readOnly = true
                )
            }
        }
        
        // Start Time Picker Dialog
        if (showStartTimePicker) {
            TimePickerDialog(
                onDismiss = { showStartTimePicker = false },
                onConfirm = { hour, minute ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    onStartTimeChange(timeFormatter.format(calendar.time))
                    showStartTimePicker = false
                }
            )
        }
        
        // End Time Picker Dialog
        if (showEndTimePicker) {
            TimePickerDialog(
                onDismiss = { showEndTimePicker = false },
                onConfirm = { hour, minute ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    onEndTimeChange(timeFormatter.format(calendar.time))
                    showEndTimePicker = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPreviewBox(
    coordinates: Pair<Double, Double>?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (coordinates != null) {
            // Display actual map with marker when coordinates are available
            val jamaicaLocation = LatLng(coordinates.first, coordinates.second)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(jamaicaLocation, 15f)
            }
            
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = jamaicaLocation),
                    title = "Selected Location"
                )
            }
        } else {
            // Display placeholder when no coordinates are selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_map_placeholder),
                    contentDescription = "Map preview",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(8.dp)
                )
                
                Text(
                    text = "Map preview",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Text(
                    text = "Pin your exact location",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState()
    
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LocationTimeStepPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            LocationTimeStep(
                location = "",
                onLocationChange = {},
                date = "",
                onDateChange = {},
                startTime = "",
                onStartTimeChange = {},
                endTime = "",
                onEndTimeChange = {},
                onCoordinatesChange = { _, _ -> }
            )
        }
    }
}
