package com.pedfu.daystreak.presentation.home.dialogs.confirmDeleteCategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch

enum class ConfirmDeleteState {
    IDLE,
    READY,
    LOADING,
    DONE,
}

class ConfirmDeleteViewModel(
    private val id: Long,
    private val type: ConfirmDeleteType,
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
) : ViewModel() {
    private var typedCategoryName: String = ""
        set(value) {
            field = value
            typedCategoryNameLiveData.value = value
        }
    private var state: ConfirmDeleteState = ConfirmDeleteState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    val categoryLiveData = MutableLiveData<StreakCategoryItem?>(null)
    val streakLiveData = MutableLiveData<List<StreakItem>?>(null)
    val singleStreakLiveData = MutableLiveData<StreakItem?>(null)
    val typedCategoryNameLiveData = MutableLiveData(typedCategoryName)
    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<Boolean?>(null)

    init {
        viewModelScope.launch {
            if (type == ConfirmDeleteType.CATEGORY) {
                categoryLiveData.value = streakRepository.getCategory(id)
                if (categoryLiveData.value?.id != null) {
                    streakLiveData.value = streakRepository.getStreaksByCategory(categoryLiveData.value!!.id)
                    state = when {
                        streakLiveData.value!!.isEmpty() -> ConfirmDeleteState.READY
                        else -> ConfirmDeleteState.IDLE
                    }
                }
            } else {
                singleStreakLiveData.value = streakRepository.getStreak(id)
                state = when {
                    singleStreakLiveData.value != null -> ConfirmDeleteState.READY
                    else -> ConfirmDeleteState.IDLE
                }
            }
        }
    }

    fun onTypeChanged(text: String) {
        typedCategoryName = text
        validateFields()
    }

    private fun validateFields() {
        state = when {
            streakLiveData.value?.isEmpty() == true || type == ConfirmDeleteType.STREAK -> ConfirmDeleteState.READY
            typedCategoryName == categoryLiveData.value?.name -> ConfirmDeleteState.READY
            else -> ConfirmDeleteState.IDLE
        }
    }

    fun onDelete() {
        if (stateLiveData.value != ConfirmDeleteState.READY) return

        viewModelScope.launch {
            try {
                state = ConfirmDeleteState.LOADING

                when (type) {
                    ConfirmDeleteType.CATEGORY -> streakUseCase.deleteCategory(id)
                    ConfirmDeleteType.STREAK -> streakUseCase.deleteStreak(id)
                }

                state = ConfirmDeleteState.DONE
            } catch (ex: Throwable) {
                errorLiveData.value = true
            }
        }
    }
}