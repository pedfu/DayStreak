package com.pedfu.daystreak.domain.notification

import com.pedfu.daystreak.domain.badge.Badge
import java.util.Date

data class NotificationItem (
    val id: Long,
    val title: String,
    val message: String?,
    val type: String?,
    var read: Boolean = false,
    var createdAt: Date,
    val badge: Badge?,
    val streakId: Long?, // get streak when click to open notification, so no need to have all data here
)
