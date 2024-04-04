package com.pedfu.daystreak.data.remote.authorization

import kotlinx.coroutines.flow.SharedFlow

interface AuthorizationManager {
    var token: String?
    val hasToken get() = token != null
    val unauthorizedFlow: SharedFlow<Unit>

    fun notifyUnauthorized()
}