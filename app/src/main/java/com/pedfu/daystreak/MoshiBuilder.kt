package com.pedfu.daystreak

import com.pedfu.daystreak.data.remote.adapters.DateAdapter
import com.pedfu.daystreak.data.remote.adapters.LoginRequestJsonAdapter
import com.pedfu.daystreak.data.remote.adapters.UserAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiBuilder {
    fun build(): Moshi {
        return Moshi.Builder()
            .add(UserAdapter)
            .add(DateAdapter)
            .add(LoginRequestJsonAdapter)
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
}