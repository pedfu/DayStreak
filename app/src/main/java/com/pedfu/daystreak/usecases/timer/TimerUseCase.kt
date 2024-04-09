package com.pedfu.daystreak.usecases.timer

import java.util.Date

class TimerUseCase() {
    suspend fun sendNewTrack(durationInSeconds: Long, startTime: Date, endTime: Date) {
        // call api
    }
}