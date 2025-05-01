package com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun TicketsSettingsStep(
    isFreeEvent: Boolean,
    onIsFreeEventChange: (Boolean) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    capacity: String,
    onCapacityChange: (String) -> Unit,
    isPrivateEvent: Boolean,
    onIsPrivateEventChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Free Event Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Free Event",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "Toggle off to set ticket prices",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Switch(
                checked = isFreeEvent,
                onCheckedChange = onIsFreeEventChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            )
        }
        
        // Price field (only shown if not free)
        if (!isFreeEvent) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Ticket Price",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->
                    // Only allow numeric input with decimal point
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        onPriceChange(newValue)
                    }
                },
                prefix = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Event Capacity
        Text(
            text = "Event Capacity",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = capacity,
            onValueChange = { newValue ->
                // Only allow numeric input
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                    onCapacityChange(newValue)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Capacity",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            },
            placeholder = { Text("Number of attendees") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        
        Text(
            text = "Leave blank for unlimited capacity",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Private Event Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Private Event",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "Only visible to invited guests",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Switch(
                checked = isPrivateEvent,
                onCheckedChange = onIsPrivateEventChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketsSettingsStepPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            TicketsSettingsStep(
                isFreeEvent = true,
                onIsFreeEventChange = {},
                price = "0.00",
                onPriceChange = {},
                capacity = "",
                onCapacityChange = {},
                isPrivateEvent = false,
                onIsPrivateEventChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketsSettingsStepPaidPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            TicketsSettingsStep(
                isFreeEvent = false,
                onIsFreeEventChange = {},
                price = "10.00",
                onPriceChange = {},
                capacity = "100",
                onCapacityChange = {},
                isPrivateEvent = true,
                onIsPrivateEventChange = {}
            )
        }
    }
}
