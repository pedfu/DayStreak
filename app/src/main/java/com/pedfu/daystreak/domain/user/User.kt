package com.pedfu.daystreak.domain.user

import android.net.Uri

class User (
    val userId: Long,
    val username: String?,
    val email: String?,
    val uid: String?,
    val photoUrl: Uri?,
    val tenantId: String?,
)