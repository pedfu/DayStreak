package com.pedfu.daystreak.presentation.reusable.localBackgroundBottomSheet

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOption
import com.pedfu.daystreak.utils.BACKGROUND_OPTIONS
import com.pedfu.daystreak.utils.ImageProvider
import kotlinx.coroutines.launch

class ModalLocalBackgroundBottomSheetViewModel(
    private val context: Context
) : ViewModel() {

    val backgroundOptionsLiveData = MutableLiveData<List<BackgroundOption>>(emptyList())

    init {
        viewModelScope.launch {
            backgroundOptionsLiveData.value = BACKGROUND_OPTIONS.map { option ->
                BackgroundOption(option, ImageProvider.loadOptimizedLocalImage(option, context))
            }
        }
    }
}