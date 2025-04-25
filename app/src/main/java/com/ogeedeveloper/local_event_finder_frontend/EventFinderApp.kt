package com.ogeedeveloper.local_event_finder_frontend

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Event Finder app.
 * 
 * This class is annotated with @HiltAndroidApp which triggers Hilt's code generation
 * and serves as the application-level dependency container.
 */
@HiltAndroidApp
class EventFinderApp : Application()
