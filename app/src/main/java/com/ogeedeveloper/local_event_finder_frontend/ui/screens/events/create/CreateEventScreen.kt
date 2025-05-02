package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.components.SimpleAppBar
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps.EventDetailsStep
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps.LocationTimeStep
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps.TicketsSettingsStep
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun CreateEventScreen(
    onNavigateBack: () -> Unit,
    onEventCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    
    // Handle successful event creation
    if (uiState.isEventCreated && uiState.createdEventId != null) {
        // Call the navigation callback to return to the events screen
        onEventCreated()
        return
    }

    Scaffold(
        topBar = {
            SimpleAppBar(
                title = "Create Event",
                onNavigateBack = {
                    if (uiState.currentStep > 1) {
                        viewModel.navigateToPreviousStep()
                    } else {
                        onNavigateBack()
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Progress indicator
            StepProgressIndicator(
                currentStep = uiState.currentStep,
                totalSteps = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Step title
            Text(
                text = when (uiState.currentStep) {
                    1 -> "Step 1 of 3: Event Details"
                    2 -> "Step 2 of 3: Location & Time"
                    3 -> "Step 3 of 3: Tickets & Settings"
                    else -> ""
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Step content
            when (uiState.currentStep) {
                1 -> EventDetailsStep(
                    title = uiState.title,
                    onTitleChange = viewModel::updateTitle,
                    description = uiState.description,
                    onDescriptionChange = viewModel::updateDescription,
                    category = uiState.category,
                    onCategoryChange = viewModel::updateCategory,
                    imageUri = uiState.imageUri,
                    onImageSelected = viewModel::updateImageUri,
                    categories = uiState.categories,
                    isCategoriesLoading = uiState.isCategoriesLoading
                )
                2 -> LocationTimeStep(
                    location = uiState.location,
                    onLocationChange = viewModel::updateLocation,
                    date = uiState.date,
                    onDateChange = viewModel::updateDate,
                    startTime = uiState.startTime,
                    onStartTimeChange = viewModel::updateStartTime,
                    endTime = uiState.endTime,
                    onEndTimeChange = viewModel::updateEndTime,
                    onCoordinatesChange = viewModel::updateCoordinates
                )
                3 -> TicketsSettingsStep(
                    isFreeEvent = uiState.isFreeEvent,
                    onIsFreeEventChange = viewModel::updateIsFreeEvent,
                    price = uiState.price,
                    onPriceChange = viewModel::updatePrice,
                    capacity = uiState.capacity,
                    onCapacityChange = viewModel::updateCapacity,
                    isPrivateEvent = uiState.isPrivateEvent,
                    onIsPrivateEventChange = viewModel::updateIsPrivateEvent
                )
            }
            
            // Display error message if any
            uiState.errorMessage?.let { errorMessage ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.currentStep > 1) {
                    Button(
                        onClick = viewModel::navigateToPreviousStep,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                if (uiState.currentStep == 3) {
                    // Last step - show Save Draft and Publish buttons
                    Button(
                        onClick = viewModel::saveEventDraft,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Save Draft",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Button(
                        onClick = {
                            viewModel.publishEvent()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Publish Event",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    // Continue button for steps 1 and 2
                    Button(
                        onClick = viewModel::navigateToNextStep,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            val isLast = i == totalSteps
            
            // Step indicator
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .weight(1f)
                    .background(
                        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        shape = if (isLast) RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp) else RoundedCornerShape(0.dp)
                    )
            )
            
            if (i < totalSteps) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            CreateEventScreen(
                onNavigateBack = {},
                onEventCreated = {}
            )
        }
    }
}
