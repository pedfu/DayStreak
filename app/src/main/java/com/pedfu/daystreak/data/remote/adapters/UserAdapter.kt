package com.pedfu.daystreak.data.remote.adapters

import com.pedfu.daystreak.data.remote.user.UserJson
import com.pedfu.daystreak.domain.user.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

object UserAdapter {
    @FromJson
    fun fromJson(json: UserJson): User = json.toUser()

    @ToJson
    fun toJson(user: User) = UserJson(user)
}