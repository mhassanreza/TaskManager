package com.bigint.taskmanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class to initialize Hilt for dependency injection.
 * This class must be registered in the AndroidManifest.xml.
 */
@HiltAndroidApp
class TaskManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Optional: Add initialization logic here if needed later (e.g., logging, analytics)
    }
}