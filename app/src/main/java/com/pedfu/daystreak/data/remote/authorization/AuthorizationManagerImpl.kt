package com.pedfu.daystreak.data.remote.authorization

import android.util.Log
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.stores.session.SessionStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthorizationManagerImpl(
    private val sessionStore: SessionStore = Inject.sessionStore,
) : AuthorizationManager {
    override var token: String? by sessionStore::authorizationToken

    private val mutableUnauthorizedFlow = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val unauthorizedFlow = mutableUnauthorizedFlow.asSharedFlow()

    override fun notifyUnauthorized() {
        val success = mutableUnauthorizedFlow.tryEmit(Unit)
        if (!success) Log.w("AUTHORIZATION", "Authorization error.")
    }
}