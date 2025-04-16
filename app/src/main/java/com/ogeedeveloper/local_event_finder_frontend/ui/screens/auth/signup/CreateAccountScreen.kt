package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup

import android.R.attr.thickness
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PasswordTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    OnboardingScreen(
        title = "Create Account",
        subtitle = "Fill in your details to get started",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Create Account",
        onPrimaryButtonClick = onContinue,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Full Name field
            AppTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                placeholder = "Placeholder",
                leadingIcon = Icons.Default.Favorite
            )

            // Email field
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "Placeholder",
                leadingIcon = Icons.Default.Favorite,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Phone number field with country code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Country selector (simplified for this example)
                CountryCodeSelector(
                    selectedCountryCode = "876",
                    onCountrySelected = { },
                    modifier = Modifier.weight(0.35f)
                )

                // Phone number input
                AppTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone Number",
                    placeholder = "1237838",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(0.65f)
                )
            }

            // Password field
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Placeholder"
            )

            // Confirm Password field
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                placeholder = "Placeholder"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Social login divider
            SocialLoginDivider()

            // Social login buttons
            SocialLoginButtons()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodeSelector(
    selectedCountryCode: String,
    onCountrySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // This is a simplified country code selector for the example
    // A real implementation would use a dropdown with country flags and codes

    com.ogeedeveloper.local_event_finder_frontend.ui.components.DropdownInput(
        selectedOption = selectedCountryCode,
        onOptionSelected = onCountrySelected,
        options = listOf("876", "123", "456", "789"),
        label = "Phone Number",
        modifier = modifier
    )
}

@Composable
fun SocialLoginDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Modifier.weight(1f)
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )

        Text(
            text = "Or continue using",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun SocialLoginButtons(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Google login button
        SocialButton(
            onClick = { /* Handle Google login */ },
            iconResId = R.drawable.ic_google,
            contentDescription = "Continue with Google"
        )

        Spacer(modifier = Modifier.padding(horizontal = 16.dp))

        // Facebook login button
        SocialButton(
            onClick = { /* Handle Facebook login */ },
            iconResId = R.drawable.facebook,
            contentDescription = "Continue with Facebook"
        )
    }
}

@Composable
fun SocialButton(
    onClick: () -> Unit,
    iconResId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    LocaleventfinderfrontendTheme {
        CreateAccountScreen(
            onBackClick = {},
            onContinue = {}
        )
    }
}