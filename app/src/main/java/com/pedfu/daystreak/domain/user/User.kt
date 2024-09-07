package com.pedfu.daystreak.domain.user

import android.net.Uri
import com.squareup.moshi.Json

class User (
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val role: String,
    val photoUrl: Uri?,
    val uuid: String,
    val maxStreak: Int
)