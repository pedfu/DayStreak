package com.pedfu.daystreak.data.remote.user

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import kotlin.reflect.KClass

abstract class RemoteService(private val moshi: Moshi) {
    protected inline fun <E : Throwable, T> parseError(errorBodyClass: KClass<E>, call: () -> T): T {
        return try {
            call()
        } catch (httpException: HttpException) {
            val response = httpException.response()
            throw when (response) {
                null -> httpException
                else -> response.errorBody()?.parseError(errorBodyClass) ?: HttpException(response)
            }
        }
    }

    protected inline fun <T> wrapError(call: () -> T): T {
        return try {
            call()
        } catch (httpException: HttpException) {
            val response = httpException.response()
            throw when (response) {
                null -> httpException
                else -> HttpException(response)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <T> wrapResponseError(call: () -> Response<T>): T {
        val response = call()
        return when {
            response.isSuccessful -> response.body() as T
            else -> throw HttpException(response)
        }
    }

    protected fun <E : Throwable> ResponseBody.parseError(errorBodyClass: KClass<E>): Throwable? {
        val adapter = moshi.adapter(errorBodyClass.java)
        return try {
            adapter.fromJson(source().peek())
        } catch (_: Throwable) {
            null
        }
    }
}