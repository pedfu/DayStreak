package com.pedfu.daystreak.data.stores.session

import android.content.SharedPreferences
import androidx.core.content.edit

private const val AUTHORIZATION_TOKEN_KEY = "authorization_token"
private const val CURRENT_USER_ID_KEY = "current_user_id"

const val SESSION_STORE = "session_store"
class SessionStoreImpl(private val prefs: SharedPreferences) : SessionStore {

    override var authorizationToken: String?
        get() = prefs.getString(AUTHORIZATION_TOKEN_KEY, null)?.let { "Token $it" }
        set(value) = prefs.edit {
            putString(AUTHORIZATION_TOKEN_KEY, value)
        }

    override var currentUserId: Long?
        get() = prefs.getLong(CURRENT_USER_ID_KEY, -1)?.takeIf { it != -1L }
        set(value) = prefs.edit {
            if (value == null) remove(CURRENT_USER_ID_KEY)
            else putLong(CURRENT_USER_ID_KEY, value)
        }

    override fun clear() = prefs.edit { clear() }
}