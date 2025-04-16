package com.ogeedeveloper.local_event_finder_frontend.ui.screens.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Welcome screen
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {
    // Welcome screen has minimal business logic
    // The ViewModel is mostly here for consistency and future expansion

    fun onGetStartedClick() {
        // Could track analytics event here
    }

    fun onSignInClick() {
        // Could track analytics event here
    }
}