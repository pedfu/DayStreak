package com.pedfu.daystreak.domain.notification

data class NotificationItem (
    val id: Long,
    val message: String,
    val picture: String? = null,
    var read: Boolean = false,
    val showButton: Boolean = false,
)
