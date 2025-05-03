package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.ui.components.BottomNavItem
import com.ogeedeveloper.local_event_finder_frontend.ui.components.EmptyState
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToEvents: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCreateEvent: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
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
                    selected = true,
                    onClick = {}
                )
                BottomNavItem(
                    icon = Icons.Outlined.Search,
                    selectedIcon = Icons.Filled.Search,
                    label = "Explore",
                    selected = false,
                    onClick = onNavigateToSearch
                )
                BottomNavItem(
                    icon = Icons.Outlined.Add,
                    selectedIcon = Icons.Filled.Add,
                    label = "Create",
                    selected = false,
                    onClick = onNavigateToCreateEvent
                )
                BottomNavItem(
                    icon = Icons.Outlined.Event,
                    selectedIcon = Icons.Filled.Event,
                    label = "Events",
                    selected = false,
                    onClick = onNavigateToEvents
                )
                BottomNavItem(
                    icon = Icons.Outlined.Person,
                    selectedIcon = Icons.Filled.Person,
                    label = "Profile",
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show loading indicator when loading
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (uiState.errorMessage != null && uiState.featuredEvents.isEmpty() && uiState.nearbyEvents.isEmpty()) {
                // Show error state if there's an error and no events
                EmptyState(
                    title = "Oops!",
                    message = uiState.errorMessage ?: "Something went wrong",
                    imageResId = R.drawable.ic_empty_events,
                    actionLabel = "Try Again",
                    onActionClick = { viewModel.refreshData() }
                )
            } else {
                // Show content when data is available
                HomeContent(
                    userName = uiState.currentUser?.fullName?.split(" ")?.firstOrNull() ?: "Guest",
                    userProfileUrl = uiState.currentUser?.profileImageUrl,
                    featuredEvents = uiState.featuredEvents,
                    nearbyEvents = uiState.nearbyEvents,
                    categories = uiState.categories,
                    onEventClick = onNavigateToEventDetails,
                    onCategorySelected = { categoryId -> viewModel.selectCategory(categoryId) },
                    selectedCategoryId = uiState.selectedCategoryId,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    userName: String,
    userProfileUrl: String?,
    featuredEvents: List<Event>,
    nearbyEvents: List<Event>,
    categories: List<Category>,
    onEventClick: (String) -> Unit,
    onCategorySelected: (Int?) -> Unit = {},
    selectedCategoryId: Int? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Header with greeting and profile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello, $userName",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Let's explore what's happening nearby",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Profile image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userProfileUrl ?: R.drawable.profile_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }
        
        // Category filters
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )
        
        CategoryFilters(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            onCategorySelected = onCategorySelected
        )
        
        // Featured Events
        Text(
            text = "Featured Events",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )
        
        if (featuredEvents.isEmpty()) {
            EmptyState(
                title = "No Featured Events",
                message = "There are no featured events at the moment",
                imageResId = R.drawable.ic_empty_events,
                modifier = Modifier.height(200.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 16.dp),
                modifier = Modifier.height(280.dp)
            ) {
                items(featuredEvents) { event ->
                    FeaturedEventCard(
                        event = event,
                        onClick = { onEventClick(event.id) },
                        modifier = Modifier.width(280.dp)
                    )
                }
            }
        }
        
        // Nearby Events
        Text(
            text = "Nearby Events",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        )
        
        if (nearbyEvents.isEmpty()) {
            EmptyState(
                title = "No Nearby Events",
                message = "There are no events nearby at the moment",
                imageResId = R.drawable.ic_empty_events,
                modifier = Modifier.height(200.dp)
            )
        } else {
            nearbyEvents.forEach { event ->
                NearbyEventItem(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        
        // Add bottom padding
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeaturedEventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val day = dateFormat.format(event.startDate)
    val month = monthFormat.format(event.startDate)
    
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box {
            // Event image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            
            // Date badge
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = month,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Online badge (if applicable)
            if (event.isOnline) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomStart)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Online",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Event details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startTime = timeFormat.format(event.startDate)
            val endTime = event.endDate?.let { timeFormat.format(it) } ?: ""
            val timeRange = if (endTime.isNotEmpty()) "$startTime - $endTime" else startTime
            
            Text(
                text = timeRange,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val formattedPrice = if (event.price > 0) {
                    "$${event.price.toInt()}"
                } else {
                    "Free"
                }
                
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CategoryFilters(
    categories: List<Category>,
    selectedCategoryId: Int? = null,
    onCategorySelected: (Int?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            CategoryChip(
                name = "All",
                isSelected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) }
            )
        }
        
        items(categories.size) { index ->
            CategoryChip(
                name = categories[index].name,
                isSelected = selectedCategoryId == categories[index].id,
                onClick = { onCategorySelected(categories[index].id) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@Composable
fun NearbyEventItem(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Event image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Event details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val time = timeFormat.format(event.startDate)
            val date = dateFormat.format(event.startDate)
            
            Text(
                text = "Today • $time",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Organizer logo/icon
                Box(
                    modifier = Modifier
                        .size(20.dp)
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
        
        // Price
        Column(
            horizontalAlignment = Alignment.End
        ) {
            val formattedPrice = if (event.price > 0) {
                "$${event.price.toInt()}"
            } else {
                "Free"
            }
            
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Sample data functions
private fun getSampleEvents(): List<Event> {
    return listOf(
        Event(
            id = "1",
            title = "Shawn Mendes The Virtual Tour in Germany 2021",
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
            title = "Tech Conference 2025",
            description = "Annual tech conference with industry leaders",
            organizer = "TechCorp",
            startDate = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), // Tomorrow
            endDate = Date(System.currentTimeMillis() + 26 * 60 * 60 * 1000),
            price = 50.0,
            category = "Business"
        ),
        Event(
            id = "3",
            title = "Food Festival",
            description = "Explore cuisines from around the world",
            organizer = "FoodLovers",
            startDate = Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000), // 3 days later
            endDate = Date(System.currentTimeMillis() + 4 * 24 * 60 * 60 * 1000),
            price = 25.0,
            category = "Food"
        )
    )
}

private fun getSampleCategories(): List<Category> {
    return listOf(
        Category(id = 1, name = "Business"),
        Category(id = 2, name = "Festival"),
        Category(id = 3, name = "Music"),
        Category(id = 4, name = "Comedy"),
        Category(id = 5, name = "Concert"),
        Category(id = 6, name = "Workshop"),
        Category(id = 7, name = "Conference"),
        Category(id = 8, name = "Exhibition")
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            HomeContent(
                userName = "Tom",
                userProfileUrl = null,
                featuredEvents = getSampleEvents(),
                nearbyEvents = getSampleEvents(),
                categories = getSampleCategories(),
                onEventClick = {},
                onCategorySelected = {},
                selectedCategoryId = null
            )
        }
    }
}
