package com.ogeedeveloper.local_event_finder_frontend.di

import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.UserRepository
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.login.LoginViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.auth.signup.CreateAccountViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.main.MainViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.interests.InterestsViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.location.LocationPermissionViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.notifications.NotificationPermissionViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification.VerifyEmailViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.onboarding.verification.VerifyPhoneViewModel
import com.ogeedeveloper.local_event_finder_frontend.ui.screens.welcome.WelcomeViewModel
import com.ogeedeveloper.local_event_finder_frontend.util.permissions.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module that provides ViewModel dependencies.
 * This module is installed in the ViewModelComponent to ensure
 * that the dependencies are scoped to the ViewModel lifecycle.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideLoginViewModel(authRepository: AuthRepository): LoginViewModel {
        return LoginViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateAccountViewModel(authRepository: AuthRepository): CreateAccountViewModel {
        return CreateAccountViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideMainViewModel(
        authRepository: AuthRepository,
        eventRepository: EventRepository
    ): MainViewModel {
        return MainViewModel(authRepository, eventRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideInterestsViewModel(userRepository: UserRepository): InterestsViewModel {
        return InterestsViewModel(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLocationPermissionViewModel(
        permissionManager: PermissionManager,
        userRepository: UserRepository
    ): LocationPermissionViewModel {
        return LocationPermissionViewModel(permissionManager, userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideNotificationPermissionViewModel(
        permissionManager: PermissionManager,
        userRepository: UserRepository
    ): NotificationPermissionViewModel {
        return NotificationPermissionViewModel(permissionManager, userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideVerifyEmailViewModel(authRepository: AuthRepository): VerifyEmailViewModel {
        return VerifyEmailViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideVerifyPhoneViewModel(authRepository: AuthRepository): VerifyPhoneViewModel {
        return VerifyPhoneViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideWelcomeViewModel(): WelcomeViewModel {
        return WelcomeViewModel()
    }
}
