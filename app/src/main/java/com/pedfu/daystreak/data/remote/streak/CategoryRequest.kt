package com.pedfu.daystreak.data.remote.streak

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CategoryRequest (
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String,
) {
    constructor(name: String): this(null, name)
}