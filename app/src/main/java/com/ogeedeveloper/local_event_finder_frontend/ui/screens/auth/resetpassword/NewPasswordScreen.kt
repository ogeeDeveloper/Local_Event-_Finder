package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.resetpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.components.LoadingButton
import com.ogeedeveloper.local_event_finder_frontend.ui.components.PasswordTextField
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordScreen(
    onBackClick: () -> Unit,
    onPasswordReset: () -> Unit,
    modifier: Modifier = Modifier,
    email: String? = null,
    resetToken: String? = null,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Set the email from navigation arguments if it's not already set
    LaunchedEffect(email) {
        if (!email.isNullOrEmpty() && uiState.email.isEmpty()) {
            viewModel.updateEmail(email)
        }
    }
    
    // Set the reset token from navigation arguments if it's not already set
    LaunchedEffect(resetToken) {
        if (!resetToken.isNullOrEmpty() && uiState.resetToken.isEmpty()) {
            viewModel.updateResetToken(resetToken)
        }
    }
    
    // Show error message if any
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    // Navigate to success screen when password is reset
    LaunchedEffect(uiState.isPasswordReset) {
        if (uiState.isPasswordReset) {
            onPasswordReset()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Reset Password") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Input your New Password",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            PasswordTextField(
                value = uiState.newPassword,
                onValueChange = { viewModel.updateNewPassword(it) },
                label = "Password",
                placeholder = "Placeholder",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = "Confirm Password",
                placeholder = "Placeholder",
                isError = uiState.newPassword != uiState.confirmPassword && uiState.confirmPassword.isNotEmpty(),
                errorMessage = if (uiState.newPassword != uiState.confirmPassword && uiState.confirmPassword.isNotEmpty()) 
                    "Passwords do not match" else null,
                keyboardActions = KeyboardActions(onDone = {
                    if (uiState.newPassword.isNotBlank() && 
                        uiState.confirmPassword.isNotBlank() && 
                        uiState.newPassword == uiState.confirmPassword) {
                        viewModel.resetPassword()
                    }
                }),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            LoadingButton(
                text = "Save Password",
                isLoading = uiState.isLoading,
                onClick = { viewModel.resetPassword() },
                enabled = uiState.newPassword.isNotBlank() && 
                         uiState.confirmPassword.isNotBlank() && 
                         uiState.newPassword == uiState.confirmPassword,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewPasswordScreenPreview() {
    LocaleventfinderfrontendTheme {
        NewPasswordScreen(
            onBackClick = {},
            onPasswordReset = {}
        )
    }
}
