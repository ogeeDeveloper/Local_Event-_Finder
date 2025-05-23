package com.ogeedeveloper.local_event_finder_frontend.di

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ogeedeveloper.local_event_finder_frontend.R
import com.ogeedeveloper.local_event_finder_frontend.data.cache.CategoryCache
import com.ogeedeveloper.local_event_finder_frontend.data.location.LocationService
import com.ogeedeveloper.local_event_finder_frontend.data.network.ApiConfig
import com.ogeedeveloper.local_event_finder_frontend.data.network.AuthApi
import com.ogeedeveloper.local_event_finder_frontend.data.network.AuthInterceptor
import com.ogeedeveloper.local_event_finder_frontend.data.network.AuthService
import com.ogeedeveloper.local_event_finder_frontend.data.network.EventApi
import com.ogeedeveloper.local_event_finder_frontend.data.network.EventDeserializer
import com.ogeedeveloper.local_event_finder_frontend.data.network.UserApi
import com.ogeedeveloper.local_event_finder_frontend.data.repository.AuthRepositoryImpl
import com.ogeedeveloper.local_event_finder_frontend.data.repository.EventRepositoryImpl
import com.ogeedeveloper.local_event_finder_frontend.data.repository.UserRepositoryImpl
import com.ogeedeveloper.local_event_finder_frontend.data.storage.AuthLocalDataSource
import com.ogeedeveloper.local_event_finder_frontend.data.storage.UserPreferences
import com.ogeedeveloper.local_event_finder_frontend.data.upload.ImageUploadService
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Event
import com.ogeedeveloper.local_event_finder_frontend.domain.model.Location
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.AuthRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.EventRepository
import com.ogeedeveloper.local_event_finder_frontend.domain.repository.UserRepository
import com.ogeedeveloper.local_event_finder_frontend.util.permissions.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Main Hilt module that provides dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): AuthLocalDataSource {
        return AuthLocalDataSource(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
        gson: Gson
    ): UserPreferences {
        return UserPreferences(context, gson)
    }

    @Provides
    @Singleton
    fun providePermissionManager(
        @ApplicationContext context: Context
    ): PermissionManager {
        return PermissionManager(context)
    }

    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig {
        return ApiConfig()
    }
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authLocalDataSource: AuthLocalDataSource
    ): AuthInterceptor {
        return AuthInterceptor(authLocalDataSource)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Create a cookie jar to handle cookies
        val cookieJar = JavaNetCookieJar(CookieManager(null, CookiePolicy.ACCEPT_ALL))
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .cookieJar(cookieJar)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, apiConfig: ApiConfig): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // Match the backend date format
            .registerTypeAdapter(Event::class.java, EventDeserializer())
            .create()
            
        return Retrofit.Builder()
            .baseUrl(apiConfig.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(authService: AuthService, apiConfig: ApiConfig): AuthApi {
        return AuthApi(authService, apiConfig)
    }

    @Provides
    @Singleton
    fun provideUserApi(apiConfig: ApiConfig): UserApi {
        return UserApi(apiConfig)
    }

    @Provides
    @Singleton
    fun provideEventApi(apiConfig: ApiConfig, retrofit: Retrofit): EventApi {
        return EventApi(apiConfig, retrofit)
    }

    @Provides
    @Singleton
    fun provideGoogleSignInOptions(@ApplicationContext context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context,
        options: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(context, options)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        authLocalDataSource: AuthLocalDataSource,
        sharedPreferences: SharedPreferences,
        googleSignInClient: GoogleSignInClient
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApi = authApi,
            authLocalDataSource = authLocalDataSource,
            sharedPreferences = sharedPreferences,
            googleSignInClient = googleSignInClient
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserApi,
        userPreferences: UserPreferences
    ): UserRepository {
        return UserRepositoryImpl(userApi, userPreferences)
    }

    @Provides
    @Singleton
    fun provideCategoryCache(): CategoryCache {
        return CategoryCache()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        eventApi: EventApi,
        categoryCache: CategoryCache
    ): EventRepository {
        return EventRepositoryImpl(eventApi, categoryCache)
    }

    @Provides
    @Singleton
    fun provideLocationService(
        @ApplicationContext context: Context
    ): LocationService {
        return LocationService(context)
    }

    @Provides
    @Singleton
    fun provideImageUploadService(
        @ApplicationContext context: Context,
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient
    ): ImageUploadService {
        return ImageUploadService(context, apiConfig, okHttpClient)
    }
}