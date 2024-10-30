package com.pedfu.daystreak

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

class DayStreakApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Inject.init(this)

        // Set a global exception handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Show an error message or dialog
            showErrorTooltip("An error occurred. Please try again.")

            // Optionally log the error (e.g., send to a logging server)
            Log.e("GlobalExceptionHandler", "Unhandled exception", throwable)
        }
    }

    private fun showErrorTooltip(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}