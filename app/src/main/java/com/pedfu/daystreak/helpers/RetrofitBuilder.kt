package com.pedfu.daystreak.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.time.Duration
import java.util.concurrent.TimeUnit

class RetrofitBuilder(
    private val readTimeout: Duration? = null
) {
    private val clientBuilder = OkHttpClient.Builder()

    fun build(): Retrofit {
//        if (readTimeout != null) {
//            clientBuilder.readTimeout(readTimeout.toMillis(), TimeUnit.MILLISECONDS)
//        }

        return Retrofit.Builder()
            .baseUrl("https://localhost:8000")
            .client(clientBuilder.build())
            .build()
    }
}