package com.pedfu.daystreak.data.remote.streak

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.RemoteService
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class StreakService(
    private val streakApi: StreakApi = Inject.streakApi,
    private val moshi: Moshi = Inject.moshi
): RemoteService(moshi) {

    suspend fun createStreak(streakRequest: StreakRequest): StreakResponse {
        return try {
            streakApi.createStreak(streakRequest)
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun createCategory(categoryRequest: CategoryRequest): CategoryResponse {
        return try {
            streakApi.createCategory(categoryRequest)
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun getStreaks(): List<StreakResponse> {
        return try {
            val response = streakApi.getStreaks()
            response.body() ?: emptyList()
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun getStreak(id: Long): StreakResponse? {
        return try {
            streakApi.getStreak(id)
        } catch (e: HttpException) {
            throw e
        }
    }
}