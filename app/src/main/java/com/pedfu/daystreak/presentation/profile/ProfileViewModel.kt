package com.pedfu.daystreak.presentation.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User

enum class ProfileState {
    IDLE,
    DELETING,
    LOADING,
    DELETED,
}

class ProfileViewModel(
    private val userRepository: UserRepository = Inject.userRepository
): ViewModel() {
    private var state = ProfileState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }
    private var newImageProfile: Uri? = null
        set(value) {
            field = value
            newImageProfileLiveData.value = value
        }
    val stateLiveData = MutableLiveData(state)
    val userLiveData: LiveData<User?> = userRepository.userFlow.asLiveData()
    val newImageProfileLiveData = MutableLiveData(newImageProfile)

    fun onSelectNewProfileImage(imageUri: Uri?) {
        newImageProfile = imageUri
        // save image in backend
    }
}