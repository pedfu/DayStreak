package com.pedfu.daystreak.data.remote.streak

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.user.RemoteService
import retrofit2.HttpException

class StreakService(
    private val streakApi: StreakApi = Inject.streakApi
): RemoteService() {

    suspend fun createStreak(streakRequest: StreakRequest): StreakResponse {
        return try {
            streakApi.createStreak(streakRequest)
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun getStreaks(): List<StreakResponse> {
        return try {
            streakApi.getStreaks()
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