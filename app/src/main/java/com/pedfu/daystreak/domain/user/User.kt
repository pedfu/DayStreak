package com.pedfu.daystreak.domain.user

import android.net.Uri

class User (
    val id: Long,
    val token: String,
    val username: String?,
    val email: String?,
    val uid: String?,
    val photoUrl: Uri?,
)