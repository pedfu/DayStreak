package com.pedfu.daystreak.helpers

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.authorization.AuthorizationManager
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration

private const val AUTHORIZATION = "Authorization"

class RetrofitBuilder(
    private val authorizationManager: AuthorizationManager?,
    private val readTimeout: Duration? = null,
    private val moshi: Moshi = Inject.moshi,
) {
    private val clientBuilder = OkHttpClient.Builder()

    fun build(): Retrofit {
        authorizationManager?.let { addAuthorizationInterceptor(it) }
        addAuthorizationErrorInterceptor()

        if (readTimeout != null) {
            configTimeout(readTimeout)
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addNetworkInterceptor(httpLoggingInterceptor)

        return Retrofit.Builder()
            .baseUrl("https://dfd4-189-123-97-138.ngrok-free.app/") // BASE URL FROM CONFIG
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(clientBuilder.build())
            .build()
    }

    private fun configTimeout(readTimeout: Duration) {
        clientBuilder.readTimeout(readTimeout)
    }

    private fun addAuthorizationInterceptor(authorizationManager: AuthorizationManager) {
        clientBuilder.addNetworkInterceptor { chain ->
            val token = authorizationManager.token ?: ""
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(AUTHORIZATION, token)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private fun addAuthorizationErrorInterceptor() {
        clientBuilder.addInterceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.code != 401) return@addInterceptor response

            when (authorizationManager) {
                null -> throw Error("ADD CUSTOM ERROR HERE") // add error here
                else -> {
                    authorizationManager.token = null
                    authorizationManager.notifyUnauthorized()
                    throw Error(" add other custom error here") // add other custom error here
                }
            }
        }
    }
}