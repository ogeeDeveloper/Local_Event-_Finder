package com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PrimaryButton
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.PrimaryLight

@Composable
fun VerifyPhoneScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    phoneNumber: String = "+1 (876) 123-4568",
    modifier: Modifier = Modifier
) {
    var verificationCode by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(false) }

    OnboardingScreen(
        title = "Verify Your Phone",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = if (isVerified) "Continue" else null,
        onPrimaryButtonClick = onContinue,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Phone icon in circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = PrimaryLight,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Verification message
            Text(
                text = "We've sent a verification code to",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone number
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Verification code input
            AppTextField(
                value = verificationCode,
                onValueChange = {
                    if (it.length <= 6) {
                        verificationCode = it
                        // Auto verify when 6 digits are entered
                        if (it.length == 6) {
                            isVerified = true
                        }
                    }
                },
                label = "Enter 4-digit code",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resend code button
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { /* Resend code logic */ }) {
                    Text(
                        text = "Resend code (30s)",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Verification status
            if (isVerified) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_my_calendar),
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Text(
                        text = "Code verified successfully!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Continue button for verified state
                if (!isVerified) {
                    PrimaryButton(
                        text = "Continue",
                        onClick = onContinue,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyPhoneScreenPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            VerifyPhoneScreen(
                onBackClick = {},
                onContinue = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyPhoneScreenVerifiedPreview() {
    LocaleventfinderfrontendTheme {
        Surface {
            var verificationCode by remember { mutableStateOf("123456") }
            var isVerified by remember { mutableStateOf(true) }

            VerifyPhoneScreen(
                onBackClick = {},
                onContinue = {}
            )
        }
    }
}