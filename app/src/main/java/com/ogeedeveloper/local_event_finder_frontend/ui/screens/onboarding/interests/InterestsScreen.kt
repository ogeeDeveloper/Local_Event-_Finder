package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.interests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme


data class InterestCategory(
    val id: String,
    val name: String,
    val icon: Int? = null
)

@Composable
fun InterestsScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample interest categories
    val interestCategories = remember {
        listOf(
            InterestCategory("music", "Music & Concerts"),
            InterestCategory("sports", "Sports & Fitness"),
            InterestCategory("food", "Food & Drinks"),
            InterestCategory("business", "Business"),
            InterestCategory("education", "Education"),
            InterestCategory("art", "Art & Culture"),
            InterestCategory("technology", "Technology"),
            InterestCategory("outdoors", "Outdoors")
        )
    }

    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    OnboardingScreen(
        title = "What type of events interest you?",
        subtitle = "Select all that apply to personalize your experience",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Continue",
        onPrimaryButtonClick = onContinue,
        secondaryButtonText = "I'll customize later",
        onSecondaryButtonClick = onContinue,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(interestCategories) { category ->
                InterestCategoryItem(
                    category = category,
                    isSelected = selectedInterests.contains(category.id),
                    onToggle = { isSelected ->
                        selectedInterests = if (isSelected) {
                            selectedInterests + category.id
                        } else {
                            selectedInterests - category.id
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun InterestCategoryItem(
    category: InterestCategory,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        modifier = modifier
            .toggleable(
                value = isSelected,
                onValueChange = onToggle
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterestsScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            InterestsScreen(
                onBackClick = {},
                onContinue = {}
            )
        }
    }
}