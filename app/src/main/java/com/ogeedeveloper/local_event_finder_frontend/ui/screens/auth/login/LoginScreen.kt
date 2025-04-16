package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.components.AppTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PasswordTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PrimaryButton
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup.SocialLoginButtons
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup.SocialLoginDivider
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.SecondaryLight


@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Login title
            Text(
                text = "Login to your account",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hi, Welcome back",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "Placeholder",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Placeholder",
                leadingIcon = Icons.Default.Lock
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remember me and forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Remember me checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe }
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )

                    Text(
                        text = "Remember me",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Forgot password link
                TextButton(onClick = { /* Handle forgot password */ }) {
                    Text(
                        text = "forgot password?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            PrimaryButton(
                text = "Login",
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Social login divider
            SocialLoginDivider()

            Spacer(modifier = Modifier.height(24.dp))

            // Social login buttons
            SocialLoginButtons()

            Spacer(modifier = Modifier.weight(1f))

            // Don't have an account? Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                TextButton(onClick = onSignUpClick) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryLight
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LocaleventfinderfrontendTheme {
        LoginScreen(
            onBackClick = {},
            onLoginSuccess = {},
            onSignUpClick = {}
        )
    }
}