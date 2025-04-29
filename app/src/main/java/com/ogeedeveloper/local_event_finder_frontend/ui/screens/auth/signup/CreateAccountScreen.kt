package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.DropdownInput
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PasswordTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.OnboardingScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error message if any
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    // Navigate to next screen when registration is successful
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onContinue()
        }
    }

    OnboardingScreen(
        title = "Create Account",
        subtitle = "Fill in your details to get started",
        showBackButton = true,
        onBackClick = onBackClick,
        primaryButtonText = "Create Account",
        onPrimaryButtonClick = { viewModel.createAccount() },
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Full Name field
            AppTextField(
                value = uiState.fullName,
                onValueChange = { viewModel.onFullNameChanged(it) },
                label = "Full Name",
                leadingIcon = Icons.Default.AccountBox,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Email field
            AppTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Phone number field with country code selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                var selectedCountryCode by remember { mutableStateOf("+1") }
                
                CountryCodeSelector(
                    selectedCountryCode = selectedCountryCode,
                    onCountrySelected = { 
                        selectedCountryCode = it
                        viewModel.onCountryCodeChanged(it)
                    },
                    modifier = Modifier.weight(0.3f)
                )
                
                AppTextField(
                    value = if (uiState.phoneNumber.startsWith("+")) {
                        uiState.phoneNumber.substringAfter(selectedCountryCode)
                    } else if (uiState.phoneNumber.isNotEmpty()) {
                        uiState.phoneNumber.substringAfter("+")
                    } else {
                        ""
                    },
                    onValueChange = { 
                        // Remove the country code if user enters it
                        val phoneNumber = if (it.startsWith("+")) {
                            it.substringAfter("+")
                        } else {
                            it
                        }
                        viewModel.onPhoneNumberChanged(phoneNumber) 
                    },
                    label = "Phone Number",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(0.7f)
                )
            }
            
            // Password field
            PasswordTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )
            
            // Confirm Password field
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = "Confirm Password",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Social login options
            SocialLoginDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
    DropdownInput(
        selectedOption = selectedCountryCode,
        onOptionSelected = onCountrySelected,
        options = listOf("+1", "+876", "+44", "+91"),
        label = "Code",
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
    IconButton(
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