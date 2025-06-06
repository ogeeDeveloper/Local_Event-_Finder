package com.ogeedeveloper.local_event_finder_frontend.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.login.LoginScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup.CreateAccountScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.resetpassword.resetPasswordGraph
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.interests.InterestsScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.location.LocationPermissionScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.notifications.NotificationPermissionScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification.VerifyEmailScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification.VerifyPhoneScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.welcome.WelcomeScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.home.HomeScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.search.SearchScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.filter.FilterScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.events.EventsScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.create.CreateEventScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.profile.ProfileScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.profile.EditPersonalInfoScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.profile.EditAccountScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.profile.SelectLanguageScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.events.details.EventDetailsScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.booking.PaymentMethodScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.booking.PaymentConfirmationScreen
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.booking.PaymentSuccessScreen

/**
 * Navigation routes for the app
 */
object AppDestinations {
    // Root destinations
    const val WELCOME_ROUTE = "welcome"
    const val LOGIN_ROUTE = "login"
    const val RESET_PASSWORD_ROUTE = "reset_password"

    // Nested graphs
    const val ONBOARDING_ROUTE = "onboarding"
    const val MAIN_ROUTE = "main"

    // Onboarding routes
    const val CREATE_ACCOUNT_ROUTE = "create_account"
    const val VERIFY_PHONE_ROUTE = "verify_phone"
    const val VERIFY_PHONE_ROUTE_WITH_ARGS = "verify_phone/{phoneNumber}/{userId}"
    const val VERIFY_EMAIL_ROUTE = "verify_email"
    const val INTERESTS_ROUTE = "interests"
    const val LOCATION_PERMISSION_ROUTE = "location_permission"
    const val NOTIFICATION_PERMISSION_ROUTE = "notification_permission"

    // Main app routes
    const val HOME_ROUTE = "home"
    const val SEARCH_ROUTE = "search"
    const val EVENTS_ROUTE = "events"
    const val PROFILE_ROUTE = "profile"
    const val CREATE_EVENT_ROUTE = "create_event"
    const val EVENT_DETAILS_ROUTE = "event_details/{eventId}"
    
    // Booking flow routes
    const val PAYMENT_METHOD_ROUTE = "payment_method/{eventId}"
    const val PAYMENT_CONFIRMATION_ROUTE = "payment_confirmation/{eventId}/{quantity}/{paymentMethod}"
    const val PAYMENT_SUCCESS_ROUTE = "payment_success/{transactionId}"
    
    // Profile routes
    const val EDIT_PERSONAL_INFO_ROUTE = "edit_personal_info"
    const val EDIT_ACCOUNT_ROUTE = "edit_account"
    const val SELECT_LANGUAGE_ROUTE = "select_language"
}

/**
 * Interface defining navigation actions used throughout the app
 */
interface NavigationActions {
    // Welcome and authentication
    fun navigateToWelcome()
    fun navigateToLogin()
    fun navigateToResetPassword()
    fun navigateToOnboarding()

    // Onboarding flow
    fun navigateToCreateAccount()
    fun navigateToVerifyPhone()
    fun navigateToVerifyPhone(phoneNumber: String, userId: String)
    fun navigateToVerifyEmail()
    fun navigateToInterests()
    fun navigateToLocationPermission()
    fun navigateToNotificationPermission()
    fun completeOnboarding()

    // Main app
    fun navigateToHome()
    fun navigateToSearch()
    fun navigateToEvents()
    fun navigateToProfile()
    fun navigateToCreateEvent()
    fun navigateToEventDetails(eventId: String)
    
    // Booking flow
    fun navigateToPaymentMethod(eventId: String)
    fun navigateToPaymentConfirmation(eventId: String, quantity: Int, paymentMethod: String)
    fun navigateToPaymentSuccess(transactionId: String)
    
    // Profile screens
    fun navigateToEditPersonalInfo()
    fun navigateToEditAccount()
    fun navigateToSelectLanguage()

    // Common actions
    fun navigateBack()
}

/**
 * Implementation of NavigationActions
 */
class NavigationActionsImpl(
    private val navController: NavHostController
) : NavigationActions {

    override fun navigateToWelcome() {
        navController.navigate(AppDestinations.WELCOME_ROUTE) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    override fun navigateToLogin() {
        navController.navigate(AppDestinations.LOGIN_ROUTE)
    }

    override fun navigateToResetPassword() {
        navController.navigate(AppDestinations.RESET_PASSWORD_ROUTE)
    }

    override fun navigateToOnboarding() {
        navController.navigate(AppDestinations.ONBOARDING_ROUTE) {
            popUpTo(AppDestinations.WELCOME_ROUTE) { inclusive = true }
        }
    }

    override fun navigateToCreateAccount() {
        navController.navigate(AppDestinations.CREATE_ACCOUNT_ROUTE)
    }

    override fun navigateToVerifyPhone() {
        navController.navigate(AppDestinations.VERIFY_PHONE_ROUTE)
    }

    override fun navigateToVerifyPhone(phoneNumber: String, userId: String) {
        navController.navigate("${AppDestinations.VERIFY_PHONE_ROUTE_WITH_ARGS.replace("{phoneNumber}", phoneNumber).replace("{userId}", userId)}")
    }

    override fun navigateToVerifyEmail() {
        navController.navigate(AppDestinations.VERIFY_EMAIL_ROUTE)
    }

    override fun navigateToInterests() {
        navController.navigate(AppDestinations.INTERESTS_ROUTE)
    }

    override fun navigateToLocationPermission() {
        navController.navigate(AppDestinations.LOCATION_PERMISSION_ROUTE)
    }

    override fun navigateToNotificationPermission() {
        navController.navigate(AppDestinations.NOTIFICATION_PERMISSION_ROUTE)
    }

    override fun completeOnboarding() {
        navController.navigate(AppDestinations.MAIN_ROUTE) {
            popUpTo(AppDestinations.ONBOARDING_ROUTE) { inclusive = true }
        }
    }

    override fun navigateToHome() {
        navController.navigate(AppDestinations.HOME_ROUTE)
    }

    override fun navigateToSearch() {
        navController.navigate(AppDestinations.SEARCH_ROUTE)
    }

    override fun navigateToEvents() {
        navController.navigate(AppDestinations.EVENTS_ROUTE)
    }

    override fun navigateToProfile() {
        navController.navigate(AppDestinations.PROFILE_ROUTE) {
            popUpTo(AppDestinations.HOME_ROUTE)
        }
    }

    override fun navigateToCreateEvent() {
        navController.navigate(AppDestinations.CREATE_EVENT_ROUTE)
    }
    
    override fun navigateToEventDetails(eventId: String) {
        val route = AppDestinations.EVENT_DETAILS_ROUTE.replace("{eventId}", eventId)
        navController.navigate(route)
    }
    
    // Booking flow
    override fun navigateToPaymentMethod(eventId: String) {
        val route = AppDestinations.PAYMENT_METHOD_ROUTE.replace("{eventId}", eventId)
        navController.navigate(route)
    }
    
    override fun navigateToPaymentConfirmation(eventId: String, quantity: Int, paymentMethod: String) {
        val route = AppDestinations.PAYMENT_CONFIRMATION_ROUTE
            .replace("{eventId}", eventId)
            .replace("{quantity}", quantity.toString())
            .replace("{paymentMethod}", paymentMethod)
        navController.navigate(route)
    }
    
    override fun navigateToPaymentSuccess(transactionId: String) {
        val route = AppDestinations.PAYMENT_SUCCESS_ROUTE.replace("{transactionId}", transactionId)
        navController.navigate(route) {
            // Clear the back stack to prevent going back to payment screens
            popUpTo(AppDestinations.MAIN_ROUTE) {
                inclusive = false
            }
        }
    }
    
    // Profile screens
    override fun navigateToEditPersonalInfo() {
        navController.navigate(AppDestinations.EDIT_PERSONAL_INFO_ROUTE)
    }
    
    override fun navigateToEditAccount() {
        navController.navigate(AppDestinations.EDIT_ACCOUNT_ROUTE)
    }
    
    override fun navigateToSelectLanguage() {
        navController.navigate(AppDestinations.SELECT_LANGUAGE_ROUTE)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }
}

/**
 * Main navigation host for the app
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.LOGIN_ROUTE 
) {
    val navigationActions = remember(navController) {
        NavigationActionsImpl(navController)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Welcome and login screens (outside of any nested graph)
        composable(route = AppDestinations.WELCOME_ROUTE) {
            WelcomeScreen(
                onGetStartedClick = { navigationActions.navigateToOnboarding() },
                onSignInClick = { navigationActions.navigateToLogin() }
            )
        }

        composable(route = AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onBackClick = { navigationActions.navigateBack() },
                onLoginSuccess = { navigationActions.navigateToHome() },
                onSignUpClick = { navigationActions.navigateToOnboarding() },
                onForgotPasswordClick = { navigationActions.navigateToResetPassword() }
            )
        }

        // Reset Password flow
        resetPasswordGraph(
            navController = navController,
            onBackToLogin = { navigationActions.navigateToLogin() }
        )

        // Onboarding flow nested navigation graph
        navigation(
            startDestination = AppDestinations.CREATE_ACCOUNT_ROUTE,
            route = AppDestinations.ONBOARDING_ROUTE
        ) {
            composable(route = AppDestinations.CREATE_ACCOUNT_ROUTE) {
                CreateAccountScreen(
                    onBackClick = { navigationActions.navigateToWelcome() },
                    onContinue = { phoneNumber, userId -> 
                        navigationActions.navigateToVerifyPhone(phoneNumber, userId) 
                    }
                )
            }

            composable(route = AppDestinations.VERIFY_PHONE_ROUTE) {
                // Use a default phone number for the original route
                // This route should only be used for testing or direct navigation
                VerifyPhoneScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onContinue = { navigationActions.navigateToVerifyEmail() },
                    phoneNumber = "+16505554567", // Default test phone number
                    userId = "12345" // Default test user ID
                )
            }

            composable(
                route = AppDestinations.VERIFY_PHONE_ROUTE_WITH_ARGS,
                arguments = listOf(
                    navArgument("phoneNumber") { type = NavType.StringType },
                    navArgument("userId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                VerifyPhoneScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onContinue = { navigationActions.navigateToVerifyEmail() },
                    phoneNumber = phoneNumber,
                    userId = userId
                )
            }

            composable(route = AppDestinations.VERIFY_EMAIL_ROUTE) {
                VerifyEmailScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onContinue = { navigationActions.navigateToInterests() }
                )
            }

            composable(route = AppDestinations.INTERESTS_ROUTE) {
                InterestsScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onContinue = { navigationActions.navigateToLocationPermission() }
                )
            }

            composable(route = AppDestinations.LOCATION_PERMISSION_ROUTE) {
                LocationPermissionScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onEnableLocation = { navigationActions.navigateToNotificationPermission() },
                    onSkip = { navigationActions.navigateToNotificationPermission() }
                )
            }

            composable(route = AppDestinations.NOTIFICATION_PERMISSION_ROUTE) {
                NotificationPermissionScreen(
                    onBackClick = { navigationActions.navigateBack() },
                    onComplete = { navigationActions.completeOnboarding() }
                )
            }
        }

        // Main app navigation graph
        navigation(
            startDestination = AppDestinations.HOME_ROUTE,
            route = AppDestinations.MAIN_ROUTE
        ) {
            composable(route = AppDestinations.HOME_ROUTE) {
                HomeScreen(
                    onNavigateToSearch = { navigationActions.navigateToSearch() },
                    onNavigateToEvents = { navigationActions.navigateToEvents() },
                    onNavigateToProfile = { navigationActions.navigateToProfile() },
                    onNavigateToCreateEvent = { navigationActions.navigateToCreateEvent() },
                    onNavigateToEventDetails = { eventId -> navigationActions.navigateToEventDetails(eventId) }
                )
            }

            composable(route = AppDestinations.SEARCH_ROUTE) {
                var showFilterDialog by remember { mutableStateOf(false) }
                
                SearchScreen(
                    onNavigateToHome = { navigationActions.navigateToHome() },
                    onNavigateToEvents = { navigationActions.navigateToEvents() },
                    onNavigateToProfile = { navigationActions.navigateToProfile() },
                    onNavigateToEventDetails = { eventId -> navigationActions.navigateToEventDetails(eventId) },
                    onNavigateToCreateEvent = { navigationActions.navigateToCreateEvent() },
                    onOpenFilter = { showFilterDialog = true }
                )
                
                if (showFilterDialog) {
                    FilterScreen(
                        onDismiss = { showFilterDialog = false },
                        onApplyFilter = { filterState ->
                            // Apply filter state to search
                            showFilterDialog = false
                        }
                    )
                }
            }

            composable(route = AppDestinations.EVENTS_ROUTE) {
                EventsScreen(
                    onNavigateToHome = { navigationActions.navigateToHome() },
                    onNavigateToSearch = { navigationActions.navigateToSearch() },
                    onNavigateToProfile = { navigationActions.navigateToProfile() },
                    onNavigateToEventDetails = { eventId -> navigationActions.navigateToEventDetails(eventId) },
                    onNavigateToCreateEvent = { navigationActions.navigateToCreateEvent() }
                )
            }

            composable(route = AppDestinations.CREATE_EVENT_ROUTE) {
                CreateEventScreen(
                    onNavigateBack = { navigationActions.navigateBack() },
                    onEventCreated = { 
                        navigationActions.navigateToEvents()
                    }
                )
            }

            composable(route = AppDestinations.EVENT_DETAILS_ROUTE,
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                EventDetailsScreen(
                    eventId = eventId,
                    onNavigateBack = navigationActions::navigateBack,
                    onBookEvent = { navigationActions.navigateToPaymentMethod(eventId) }
                )
            }

            composable(route = AppDestinations.PAYMENT_METHOD_ROUTE,
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                PaymentMethodScreen(
                    eventId = eventId,
                    onBackClick = navigationActions::navigateBack,
                    onProceedToConfirmation = { id, quantity, paymentMethod ->
                        navigationActions.navigateToPaymentConfirmation(id, quantity, paymentMethod)
                    }
                )
            }

            composable(route = AppDestinations.PAYMENT_CONFIRMATION_ROUTE,
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType },
                    navArgument("quantity") { type = NavType.IntType },
                    navArgument("paymentMethod") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
                val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: "Paypal"
                PaymentConfirmationScreen(
                    eventId = eventId,
                    quantity = quantity,
                    paymentMethod = paymentMethod,
                    onBackClick = navigationActions::navigateBack,
                    onConfirmPayment = { transactionId ->
                        navigationActions.navigateToPaymentSuccess(transactionId)
                    }
                )
            }

            composable(route = AppDestinations.PAYMENT_SUCCESS_ROUTE,
                arguments = listOf(
                    navArgument("transactionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId") ?: ""
                PaymentSuccessScreen(
                    transactionId = transactionId,
                    onBackToHome = navigationActions::navigateToHome
                )
            }

            composable(route = AppDestinations.PROFILE_ROUTE) {
                ProfileScreen(
                    onNavigateToHome = { navigationActions.navigateToHome() },
                    onNavigateToSearch = { navigationActions.navigateToSearch() },
                    onNavigateToCreateEvent = { navigationActions.navigateToCreateEvent() },
                    onNavigateToEvents = { navigationActions.navigateToEvents() },
                    onNavigateToEditPersonalInfo = { navigationActions.navigateToEditPersonalInfo() },
                    onNavigateToEditAccount = { navigationActions.navigateToEditAccount() },
                    onNavigateToSelectLanguage = { navigationActions.navigateToSelectLanguage() },
                    onSignOut = { navigationActions.navigateToLogin() }
                )
            }
            
            // Profile related screens
            composable(route = AppDestinations.EDIT_PERSONAL_INFO_ROUTE) {
                EditPersonalInfoScreen(
                    onNavigateBack = { navigationActions.navigateBack() }
                )
            }
            
            composable(route = AppDestinations.EDIT_ACCOUNT_ROUTE) {
                EditAccountScreen(
                    onNavigateBack = { navigationActions.navigateBack() }
                )
            }
            
            composable(route = AppDestinations.SELECT_LANGUAGE_ROUTE) {
                SelectLanguageScreen(
                    onNavigateBack = { navigationActions.navigateBack() }
                )
            }
        }
    }
}
