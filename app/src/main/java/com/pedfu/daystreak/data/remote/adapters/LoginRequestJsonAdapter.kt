package com.pedfu.daystreak.data.remote.adapters

import com.pedfu.daystreak.data.remote.login.LoginRequest
import com.pedfu.daystreak.data.remote.user.UserJson
import com.pedfu.daystreak.domain.user.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

object LoginRequestJsonAdapter {
    @FromJson
    fun fromJson(json: LoginRequest): LoginRequest = json

    @ToJson
    fun toJson(json: LoginRequest) = json
}