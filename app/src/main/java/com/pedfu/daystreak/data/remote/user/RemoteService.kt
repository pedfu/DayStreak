package com.pedfu.daystreak.data.remote.user

import retrofit2.HttpException
import kotlin.reflect.KClass

abstract class RemoteService {
    protected inline fun <E: Throwable, T> parseError(errorBodyClass: KClass<E>, call: () -> T): T {
        return try {
            call()
        } catch (httpException: HttpException) {
            val response = httpException.response()
            throw httpException
        }
    }
}