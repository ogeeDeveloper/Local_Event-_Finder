package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun LocationPermissionScreen(
    onBackClick: () -> Unit,
    onEnableLocation: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingScreen(
        title = "Find the best events near you",
        subtitle = "We'll show you events happening in your area and notify you when something exciting is nearby",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Enable Location",
        onPrimaryButtonClick = onEnableLocation,
        secondaryButtonText = "Set Location Manually",
        onSecondaryButtonClick = { /* Show location picker */ },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        ) {
            // Location illustration
            Image(
                painter = painterResource(id = R.drawable.location_illustration),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Skip button at the bottom
            TextButton(
                onClick = onSkip,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Skip for now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationPermissionScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            LocationPermissionScreen(
                onBackClick = {},
                onEnableLocation = {},
                onSkip = {}
            )
        }
    }
}