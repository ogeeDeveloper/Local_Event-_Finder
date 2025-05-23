package com.ogeedeveloper.local_event_finder_frontend.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.components.LoadingButton
import com.ogeedeveloper.local_event_finder_frontend.ui.components.OutlinedAppButton
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PrimaryButton

/**
 * Base template for onboarding screens with common layout elements
 */
@Composable
fun OnboardingScreen(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    primaryButtonText: String? = "Continue",
    primaryButtonIcon: Boolean = true,
    onPrimaryButtonClick: () -> Unit = {},
    secondaryButtonText: String? = null,
    onSecondaryButtonClick: () -> Unit = {},
    skipButtonText: String? = null,
    onSkipClick: () -> Unit = {},
    isLoading: Boolean = false,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                OnboardingTopBar(
                    title = title,
                    subtitle = subtitle,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Main content
                content()

                Spacer(modifier = Modifier.weight(1f))

                // Footer with buttons
                OnboardingFooter(
                    primaryButtonText = primaryButtonText,
                    primaryButtonIcon = primaryButtonIcon,
                    onPrimaryButtonClick = onPrimaryButtonClick,
                    secondaryButtonText = secondaryButtonText,
                    onSecondaryButtonClick = onSecondaryButtonClick,
                    skipButtonText = skipButtonText,
                    onSkipClick = onSkipClick,
                    isLoading = isLoading
                )
            }

            // Show snackbar if provided
            snackbarHostState?.let { hostState ->
                SnackbarHost(
                    hostState = hostState,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                )
            }
        }
    }
}

/**
 * Reusable top bar for onboarding screens
 */
@Composable
fun OnboardingTopBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (showBackButton) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Reusable footer with buttons for onboarding screens
 */
@Composable
fun OnboardingFooter(
    primaryButtonText: String? = null,
    primaryButtonIcon: Boolean = true,
    onPrimaryButtonClick: () -> Unit = {},
    secondaryButtonText: String? = null,
    onSecondaryButtonClick: () -> Unit = {},
    skipButtonText: String? = null,
    onSkipClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (primaryButtonText != null) {
            if (isLoading) {
                LoadingButton(
                    text = primaryButtonText,
                    onClick = onPrimaryButtonClick,
                    isLoading = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    text = primaryButtonText,
                    onClick = onPrimaryButtonClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (secondaryButtonText != null) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedAppButton(
                text = secondaryButtonText,
                onClick = onSecondaryButtonClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (skipButtonText != null) {
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onSkipClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = skipButtonText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Fix for the clickable extension function - remove it if it's not being used
// or implement it properly as a Composable function
/* If needed, implement it like this:
@Composable
fun Modifier.clickableText(onClick: () -> Unit): Modifier {
    return this.clickable(onClick = onClick)
}
*/