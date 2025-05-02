package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.ui.components.BottomNavItem
import com.ogeedeveloper.local_event_finder_frontend.ui.components.EmptyState
import com.ogeedeveloper.local_event_finder_frontend.ui.components.SimpleAppBar
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit,
    onNavigateToCreateEvent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateEvent,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Event"
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomNavItem(
                    icon = Icons.Outlined.Home,
                    selectedIcon = Icons.Filled.Home,
                    label = "Home",
                    selected = false,
                    onClick = onNavigateToHome
                )
                BottomNavItem(
                    icon = Icons.Outlined.Search,
                    selectedIcon = Icons.Filled.Search,
                    label = "Explore",
                    selected = false,
                    onClick = onNavigateToSearch
                )
                BottomNavItem(
                    icon = Icons.Outlined.Event,
                    selectedIcon = Icons.Filled.Event,
                    label = "Events",
                    selected = true,
                    onClick = {}
                )
                BottomNavItem(
                    icon = Icons.Outlined.Person,
                    selectedIcon = Icons.Filled.Person,
                    label = "Profile",
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            EventsContent(
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                upcomingEvents = uiState.upcomingEvents,
                pastEvents = uiState.pastEvents,
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = viewModel::selectTab,
                onEventClick = onNavigateToEventDetails,
                onRefresh = viewModel::refreshEvents,
                onCreateEventClick = onNavigateToCreateEvent,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EventsContent(
    isLoading: Boolean,
    errorMessage: String?,
    upcomingEvents: List<Event>,
    pastEvents: List<Event>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onEventClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onCreateEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tab selector
        val tabs = listOf("Upcoming", "Past Event")
        
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {},
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                Tab(
                    selected = selected,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface
                        )
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event list with pull-to-refresh
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val events = if (selectedTabIndex == 0) upcomingEvents else pastEvents
            
            if (isLoading && events.isEmpty()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (errorMessage != null) {
                EmptyState(
                    title = "Oops!",
                    message = errorMessage,
                    imageResId = R.drawable.ic_empty_events,
                    actionLabel = "Try Again",
                    onActionClick = onRefresh
                )
            } else if (events.isEmpty()) {
                EmptyState(
                    title = "No ${if (selectedTabIndex == 0) "Upcoming" else "Past"} Events",
                    message = if (selectedTabIndex == 0) 
                        "There are no upcoming events at the moment. Create one now!" 
                    else 
                        "You haven't attended any events yet.",
                    imageResId = R.drawable.ic_empty_events,
                    actionLabel = if (selectedTabIndex == 0) "Create Event" else null,
                    onActionClick = if (selectedTabIndex == 0) onCreateEventClick else null
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(events) { event ->
                        EventItem(
                            event = event,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event image or placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Event details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Date and time
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeString = dateFormat.format(event.startDate)
                
                Text(
                    text = "Today • $timeString",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Event title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Organizer
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Organizer logo/icon
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = event.organizer.take(1),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = event.organizer,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Price
            Text(
                text = "$${event.price.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Sample data functions - these would be removed in a real app with actual API calls
private fun getSampleEvents(): List<Event> {
    return listOf(
        Event(
            id = "1",
            title = "Shawn Mendes The Virtual Tour 2021 in Germany",
            description = "Join Shawn Mendes for a virtual concert experience",
            organizer = "Microsoft",
            startDate = Date(),
            endDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
            isOnline = true,
            price = 100.0,
            category = "Music"
        ),
        Event(
            id = "2",
            title = "Shawn Mendes The Virtual Tour 2021 in Germany",
            description = "Join Shawn Mendes for a virtual concert experience",
            organizer = "Microsoft",
            startDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
            endDate = Date(System.currentTimeMillis() + 4 * 60 * 60 * 1000),
            isOnline = true,
            price = 100.0,
            category = "Music"
        ),
        Event(
            id = "2free",
            title = "Community Yoga Session",
            description = "Join our free community yoga session in the park",
            organizer = "Yoga Community",
            startDate = Date(),
            endDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
            isOnline = false,
            price = 0.0, // Free event
            category = "Wellness"
        )
    )
}

private fun getSamplePastEvents(): List<Event> {
    return listOf(
        Event(
            id = "3",
            title = "Tech Conference 2025",
            description = "Annual tech conference with industry leaders",
            organizer = "TechCorp",
            startDate = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), // Yesterday
            endDate = Date(System.currentTimeMillis() - 22 * 60 * 60 * 1000),
            price = 50.0,
            category = "Business"
        ),
        Event(
            id = "4",
            title = "Food Festival",
            description = "Explore cuisines from around the world",
            organizer = "FoodLovers",
            startDate = Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000), // 3 days ago
            endDate = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000),
            price = 25.0,
            category = "Food"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            EventsContent(
                isLoading = false,
                errorMessage = "",
                upcomingEvents = getSampleEvents(),
                pastEvents = getSamplePastEvents(),
                selectedTabIndex = 0,
                onTabSelected = {},
                onEventClick = {},
                onRefresh = {},
                onCreateEventClick = {},
                modifier = Modifier
            )
        }
    }
}
