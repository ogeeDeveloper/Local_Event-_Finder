package com.ogeedeveloper.local_event_finder_frontend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Main Hilt module that provides dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}