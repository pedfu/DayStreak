package com.pedfu.daystreak.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.local.user.UserEntity
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User

class HomeViewModel(
    private val userRepository: UserRepository = Inject.userRepository
): ViewModel() {

    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
}