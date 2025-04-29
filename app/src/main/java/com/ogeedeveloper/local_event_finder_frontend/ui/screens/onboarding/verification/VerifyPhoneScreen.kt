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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.domain.model.VerificationStatus
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.PrimaryLight

@Composable
fun VerifyPhoneScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    phoneNumber: String,
    userId: String,
    modifier: Modifier = Modifier,
    viewModel: VerifyPhoneViewModel = hiltViewModel()
) {
    // Set the phone number and userId in the ViewModel
    LaunchedEffect(phoneNumber, userId) {
        if (phoneNumber.isNotEmpty() && userId.isNotEmpty()) {
            viewModel.setPhoneNumberAndUserId(phoneNumber, userId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error message if any
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // Navigate to next screen when verification is successful
    LaunchedEffect(uiState.verificationStatus) {
        if (uiState.verificationStatus == VerificationStatus.VERIFIED) {
            onContinue()
        }
    }

    OnboardingScreen(
        title = "Verify Your Phone",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = if (uiState.verificationStatus == VerificationStatus.CODE_ENTERED) "Verify" else null,
        onPrimaryButtonClick = { viewModel.verifyPhoneNumber() },
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
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
                value = uiState.verificationCode,
                onValueChange = { 
                    viewModel.onVerificationCodeChanged(it)
                },
                label = "Verification Code",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resend code button
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { viewModel.sendVerificationCode() },
                    enabled = uiState.resendCountdown == 0
                ) {
                    Text(
                        text = if (uiState.resendCountdown > 0) 
                            "Resend code (${uiState.resendCountdown}s)" 
                        else 
                            "Resend code",
                        color = if (uiState.resendCountdown > 0)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Verification status
            if (uiState.verificationStatus == VerificationStatus.VERIFIED) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
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
                onContinue = {},
                phoneNumber = "+1 (876) 123-4568",
                userId = "12345"
            )
        }
    }
}