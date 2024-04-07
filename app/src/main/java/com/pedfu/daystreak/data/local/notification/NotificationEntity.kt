package com.pedfu.daystreak.data.local.notification

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pedfu.daystreak.data.local.streak.StreakEntity
import com.pedfu.daystreak.domain.badge.Badge
import com.pedfu.daystreak.domain.notification.NotificationItem
import java.util.Date

@Entity(
    tableName = "notification",
    foreignKeys = [
        ForeignKey(
            entity = StreakEntity::class,
            parentColumns = ["id"],
            childColumns = ["streakId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["streakId"]),
    ]
)
class NotificationEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val message: String?,
    val type: String?,
    val read: Boolean,
    val createdAt: Date,
    @Embedded(prefix = "badge") val badge: Badge?,
    val streakId: Long?
) {
    constructor(notification: NotificationItem): this(
        id = notification.id,
        title = notification.title,
        message = notification.message,
        type = notification.type,
        read = notification.read,
        createdAt = notification.createdAt,
        badge = notification.badge,
        streakId = notification.streakId,
    )

    fun toNotification(): NotificationItem = NotificationItem(
        id,
        title,
        message,
        type,
        read,
        createdAt,
        badge,
        streakId
    )
}