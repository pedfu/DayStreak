package com.pedfu.daystreak

import android.app.Application

class DayStreakApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Inject.init(this)
    }
}