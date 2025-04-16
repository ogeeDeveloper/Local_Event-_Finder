package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

data class NotificationOption(
    val id: String,
    val title: String,
    val description: String
)

@Composable
fun NotificationPermissionScreen(
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Notification options
    val notificationOptions = remember {
        listOf(
            NotificationOption(
                id = "new_events",
                title = "New events matching your interests",
                description = "Be the first to know about new events you might like"
            ),
            NotificationOption(
                id = "price_drops",
                title = "Price drops for saved events",
                description = "Get notified when tickets go on sale"
            ),
            NotificationOption(
                id = "friend_events",
                title = "Events friends are attending",
                description = "Find out what's popular in your social circle"
            ),
            NotificationOption(
                id = "reminders",
                title = "Reminders before booked events",
                description = "Never miss an event you've booked"
            )
        )
    }

    var selectedOptions by remember {
        mutableStateOf(setOf("friend_events", "reminders"))
    }

    OnboardingScreen(
        title = "Stay Updated",
        subtitle = "Choose what notifications you'd like to receive",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Complete Setup",
        onPrimaryButtonClick = onComplete,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(vertical = 16.dp)
        ) {
            notificationOptions.forEach { option ->
                NotificationOptionItem(
                    option = option,
                    isSelected = selectedOptions.contains(option.id),
                    onToggle = { isSelected ->
                        selectedOptions = if (isSelected) {
                            selectedOptions + option.id
                        } else {
                            selectedOptions - option.id
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun NotificationOptionItem(
    option: NotificationOption,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onToggle(!isSelected) },
                role = Role.Checkbox
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null // null because we're handling it with selectable
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = option.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPermissionScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            NotificationPermissionScreen(
                onBackClick = {},
                onComplete = {}
            )
        }
    }
}