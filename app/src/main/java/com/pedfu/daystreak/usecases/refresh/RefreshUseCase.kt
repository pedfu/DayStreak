package com.pedfu.daystreak.usecases.refresh

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn

class RefreshUseCase(
    private val streakUseCase: StreakUseCase = Inject.streakUseCase
) {
    private val coroutineScope = MainScope()
    private val mutableRefreshingFlow = MutableStateFlow(false)
    val isRefreshingFlow = mutableRefreshingFlow.asStateFlow()

    private val refreshStreaksFlow = flow {
        emit(fetchStreaks())
    }.shareIn(coroutineScope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0))

    private var isRefreshingStreak: Boolean = false
        set(value) {
            field = value
            mutableRefreshingFlow.value = value
        }

    suspend fun refresh() {
        coroutineScope {
            val streaksAsync = async { refreshStreaks() }
            streaksAsync.join()
        }
    }

    private suspend fun refreshStreaks() {
        refreshStreaksFlow.firstOrNull()
    }

    private suspend fun fetchStreaks(): Throwable? {
        isRefreshingStreak = true

        try {
            streakUseCase.fetchStreaks()
        } catch (throwable: Throwable) {
            return throwable
        } finally {
            isRefreshingStreak = false
        }

        return null
    }
}