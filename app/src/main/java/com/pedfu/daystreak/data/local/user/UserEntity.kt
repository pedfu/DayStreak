package com.pedfu.daystreak.data.local.user

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedfu.daystreak.domain.user.User

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String,
    val photoUri: String?,
    val uuid: String,
    val maxStreak: Int,
) {
    constructor(user: User): this(
        user.id,
        user.email,
        user.firstName,
        user.lastName,
        user.username,
        user.role,
        user.photoUrl.toString(),
        user.uuid,
        user.maxStreak,
    )

    fun toUser(): User = User(
        id,
        email,
        firstName,
        lastName,
        username,
        role,
        Uri.parse(photoUri),
        uuid,
        maxStreak,
    )
}