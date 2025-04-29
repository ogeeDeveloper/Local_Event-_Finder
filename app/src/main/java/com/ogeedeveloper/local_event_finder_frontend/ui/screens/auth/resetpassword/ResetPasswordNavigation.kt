package com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

// Reset password route constants
object ResetPasswordDestinations {
    const val RESET_PASSWORD_ROUTE = "reset_password"
    const val EMAIL_SCREEN = "email_screen"
    const val CONFIRMATION_SCREEN = "confirmation_screen/{email}"
    const val NEW_PASSWORD_SCREEN = "new_password_screen/{email}/{resetToken}"
    const val SUCCESS_SCREEN = "success_screen"
    
    // Helper functions to create routes with arguments
    fun confirmationScreenRoute(email: String): String = 
        "confirmation_screen/${email}"
        
    fun newPasswordScreenRoute(email: String, resetToken: String): String = 
        "new_password_screen/${email}/${resetToken}"
}

/**
 * Adds reset password flow navigation graph to the NavGraphBuilder
 */
fun NavGraphBuilder.resetPasswordGraph(
    navController: NavHostController,
    onBackToLogin: () -> Unit
) {
    navigation(
        startDestination = ResetPasswordDestinations.EMAIL_SCREEN,
        route = ResetPasswordDestinations.RESET_PASSWORD_ROUTE
    ) {
        composable(ResetPasswordDestinations.EMAIL_SCREEN) {
            ResetPasswordEmailScreen(
                onBackClick = onBackToLogin,
                onCodeSent = { email ->
                    navController.navigate(ResetPasswordDestinations.confirmationScreenRoute(email))
                }
            )
        }
        
        composable(ResetPasswordDestinations.CONFIRMATION_SCREEN) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            EmailConfirmationScreen(
                email = email,
                onBackClick = {
                    navController.popBackStack()
                },
                onCodeVerified = { resetToken ->
                    navController.navigate(ResetPasswordDestinations.newPasswordScreenRoute(email!!, resetToken))
                }
            )
        }
        
        composable(ResetPasswordDestinations.NEW_PASSWORD_SCREEN) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val resetToken = backStackEntry.arguments?.getString("resetToken")
            NewPasswordScreen(
                email = email,
                resetToken = resetToken,
                onBackClick = {
                    navController.popBackStack()
                },
                onPasswordReset = {
                    navController.navigate(ResetPasswordDestinations.SUCCESS_SCREEN) {
                        // Clear the back stack so user can't go back to previous screens
                        popUpTo(ResetPasswordDestinations.EMAIL_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(ResetPasswordDestinations.SUCCESS_SCREEN) {
            ResetPasswordSuccessScreen(
                onGoToLogin = onBackToLogin
            )
        }
    }
}

/**
 * Standalone reset password flow that can be used for testing or preview
 */
@Composable
fun ResetPasswordFlow(
    onBackToLogin: () -> Unit = {}
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = ResetPasswordDestinations.RESET_PASSWORD_ROUTE
    ) {
        resetPasswordGraph(
            navController = navController,
            onBackToLogin = onBackToLogin
        )
    }
}
