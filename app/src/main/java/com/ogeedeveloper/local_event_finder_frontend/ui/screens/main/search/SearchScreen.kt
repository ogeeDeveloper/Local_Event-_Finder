package com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Category
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.BottomNavBar
import com.ogeedeveloper.local_event_finder_frontend.ui.components.BottomNavItem
import com.ogeedeveloper.local_event_finder_frontend.ui.components.SearchTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToEvents: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEventDetails: (String) -> Unit,
    onOpenFilter: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavItem(
                title = "Search",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search
            ),
            BottomNavItem(
                title = "Events",
                selectedIcon = Icons.Filled.Event,
                unselectedIcon = Icons.Outlined.Event
            ),
            BottomNavItem(
                title = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            )
        )
    }

    var selectedTabIndex by remember { mutableIntStateOf(1) } // Search tab selected by default

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentTab = selectedTabIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> {} // Already on Search
                        2 -> onNavigateToEvents()
                        3 -> onNavigateToProfile()
                    }
                },
                items = bottomNavItems
            )
        }
    ) { paddingValues ->
        SearchContent(
            location = "Kingston, Jamaica",
            categories = getSampleCategories(),
            events = getSampleEvents(),
            onOpenFilter = onOpenFilter,
            onEventClick = onNavigateToEventDetails,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    location: String,
    categories: List<Category>,
    events: List<Event>,
    onOpenFilter: () -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Location selector
        LocationSelector(
            location = location,
            onLocationClick = { /* Navigate to location selection */ }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "",
                onValueChange = { /* Update search query */ },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your event here") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onOpenFilter)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter_custom),
                                contentDescription = "Filter",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Category filters
        CategoryFilters(
            categories = categories,
            selectedCategoryIndex = 0,
            onCategorySelected = { /* Update selected category */ }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}

@Composable
fun LocationSelector(
    location: String,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "My Location",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onLocationClick)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Text(
                text = location,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Change location",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CategoryFilters(
    categories: List<Category>,
    selectedCategoryIndex: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            CategoryChip(
                name = "All",
                isSelected = selectedCategoryIndex == 0,
                onClick = { onCategorySelected(0) }
            )
        }
        
        items(categories.size) { index ->
            CategoryChip(
                name = categories[index].name,
                isSelected = selectedCategoryIndex == index + 1,
                onClick = { onCategorySelected(index + 1) }
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
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box {
                // Event image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                
                // Date badge
                DateBadge(
                    day = SimpleDateFormat("dd", Locale.getDefault()).format(event.startDate),
                    month = SimpleDateFormat("MMM", Locale.getDefault()).format(event.startDate),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Organizer and online badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.organizer,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (event.isOnline) {
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Event title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                    
                    event.originalPrice?.let {
                        if (it > event.price) {
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "$${it.toInt()}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateBadge(
    day: String,
    month: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
}

// Sample data functions - these would be removed in a real app with actual API calls
private fun getSampleEvents(): List<Event> {
    return listOf(
        Event(
            id = "1",
            title = "Shawn Mendes The Virtual Tour in Germany 2021",
            description = "Join Shawn Mendes for a virtual concert experience",
            organizer = "Coldplay Ticket",
            startDate = Date(),
            endDate = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // 2 hours later
            isOnline = true,
            price = 100.0,
            originalPrice = 150.0,
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
        Category(id = "1", name = "Business"),
        Category(id = "2", name = "Festival"),
        Category(id = "3", name = "Festival"),
        Category(id = "4", name = "Comedy")
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            SearchContent(
                location = "Kingston, Jamaica",
                categories = getSampleCategories(),
                events = getSampleEvents(),
                onOpenFilter = {},
                onEventClick = {}
            )
        }
    }
}
