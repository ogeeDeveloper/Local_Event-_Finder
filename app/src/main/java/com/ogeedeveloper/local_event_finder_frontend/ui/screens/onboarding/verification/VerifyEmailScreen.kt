package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.PrimaryLight

@Composable
private fun InstructionText(
    number: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun VerifyEmailScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    email: String = "tom.cruise@gmail.com",
    modifier: Modifier = Modifier
) {
    OnboardingScreen(
        title = "Verify Your Email",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Open Email App",
        onPrimaryButtonClick = onContinue,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Email icon in circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = PrimaryLight,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Verification message
            Text(
                text = "We've sent a verification link to",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email address
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Instructions
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Next Steps:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                InstructionText(number = 1, text = "Check your email inbox")
                InstructionText(number = 2, text = "Click the verification link in the email")
                InstructionText(number = 3, text = "Return to this app to continue")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Warning message
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(top = 2.dp, end = 8.dp)
                            .size(16.dp)
                    )

                    Text(
                        text = "If you don't see the email in your inbox, please check your spam folder or request a new verification email.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resend verification link
            TextButton(onClick = { /* Resend verification logic */ }) {
                Text(
                    text = "Resend verification",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // "I'll customize later" button at the bottom
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        TextButton(onClick = onContinue) {
            Text(
                text = "I'll customize later",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VerifyEmailScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            VerifyEmailScreen(
                onBackClick = {},
                onContinue = {}
            )
        }
    }
}