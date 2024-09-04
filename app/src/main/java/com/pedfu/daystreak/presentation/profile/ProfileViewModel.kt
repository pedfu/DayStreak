package com.pedfu.daystreak.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.user.UserRepository
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.usecases.user.UserUseCase
import com.pedfu.daystreak.utils.ImageProvider
import kotlinx.coroutines.launch
import java.io.File

enum class ProfileState {
    IDLE,
    DELETING,
    LOADING,
    DELETED,
}

class ProfileViewModel(
    private val context: Context,
    private val userRepository: UserRepository = Inject.userRepository,
    private val userUseCase: UserUseCase = Inject.userUseCase
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
        val user = userLiveData.value ?: return

        // save image in backend
        if (newImageProfile != null) {
            val bitmap = ImageProvider.getBitmapFromUri(context, newImageProfile!!) ?: return
            val compressedImage = ImageProvider.compressBitmap(bitmap, 80)
            val tempFile = File(context.cacheDir, "profile-picture-${user.username}-${user.uuid}.jpeg")
            tempFile.writeBytes(compressedImage)

            // call api
            viewModelScope.launch {
                // adicionar o loading por cima da foto
                userRepository.updateProfilePicture(tempFile)
            }
        }
    }
}