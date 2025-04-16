package com.ogeedeveloper.local_event_finder_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.ogeedeveloper.local_event_finder_frontend.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme

/**
 * Main entry point for the application.
 *
 * This activity sets up the Compose UI and initializes the navigation.
 * It uses Hilt for dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LocaleventfinderfrontendTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Set up the NavController
                    val navController = rememberNavController()

                    // Set up the navigation host with the NavController
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}