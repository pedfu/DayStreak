package com.pedfu.daystreak.data.stores.session

interface SessionStore {
    var authorizationToken: String?
    var currentUserId: Long?

    fun clear()
}