package com.pedfu.daystreak.usecases.refresh

import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.usecases.notification.NotificationUseCase
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
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
    private val notificationsUseCase: NotificationUseCase = Inject.notificationUseCase,
) {
    private val coroutineScope = MainScope()
    private val mutableRefreshingFlow = MutableStateFlow(false)
    val isRefreshingFlow = mutableRefreshingFlow.asStateFlow()

    private val refreshStreaksFlow = flow {
        emit(fetchStreaks())
    }.shareIn(coroutineScope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0))
    private val refreshNotificationsFlow = flow {
        emit(fetchNotifications())
    }.shareIn(coroutineScope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0))

    private val isRefreshing: Boolean
        get() = isRefreshingStreak || isRefreshingNotification

    private var isRefreshingStreak: Boolean = false
        set(value) {
            field = value
            mutableRefreshingFlow.value = isRefreshing
        }
    private var isRefreshingNotification: Boolean = false
        set(value) {
            field = value
            mutableRefreshingFlow.value = isRefreshing
        }

    suspend fun refresh() {
        coroutineScope {
            val notificationsAsync = async { refreshNotification() }
            val streaksAsync = async { refreshStreaks() }
            notificationsAsync.join()
            streaksAsync.join()
        }
    }

    private suspend fun refreshStreaks() {
        refreshStreaksFlow.firstOrNull()
    }
    private suspend fun refreshNotification() {
        refreshNotificationsFlow.firstOrNull()
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

    private suspend fun fetchNotifications(): Throwable? {
        isRefreshingNotification = true

        try {
            notificationsUseCase.fetchNotifications()
        } catch (throwable: Throwable) {
            return throwable
        } finally {
            isRefreshingNotification = false
        }

        return null
    }
}