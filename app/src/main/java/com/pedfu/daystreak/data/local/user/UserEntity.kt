package com.pedfu.daystreak.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedfu.daystreak.domain.user.User

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val userId: Long,
    val username: String?,
    val email: String?,
    val uid: String?,
    val tenantId: String?,
    val photoUri: String?,
) {
    constructor(user: User): this(
        userId = user.userId,
        username = user.username,
        email = user.email,
        uid = user.uid,
        tenantId = user.tenantId,
        photoUri = user.photoUrl.toString()
    )
}