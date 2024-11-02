package com.pedfu.daystreak.presentation.creation.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedfu.daystreak.Inject
import com.pedfu.daystreak.data.remote.streak.CategoryRequest
import com.pedfu.daystreak.data.repositories.streak.StreakRepository
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.usecases.streak.StreakUseCase
import kotlinx.coroutines.launch

const val EMPTY_NAME = "empty_name"
const val EXISTING_NAME = "existing_name"
const val NETWORK = "network"

enum class CategoryCreationState {
    IDLE,
    READY,
    LOADING,
    DONE,
}

class CategoryCreationDialogViewModel(
    private val streakRepository: StreakRepository = Inject.streakRepository,
    private val streakUseCase: StreakUseCase = Inject.streakUseCase,
) : ViewModel() {
    private var categoryName: String = ""
        set(value) {
            field = value
            categoryNameLiveData.value = value
            validateFields()
        }
    private var state: CategoryCreationState = CategoryCreationState.IDLE
        set(value) {
            field = value
            stateLiveData.value = value
        }

    val categoryNameLiveData = MutableLiveData(categoryName)
    val stateLiveData = MutableLiveData(state)
    val errorLiveData = MutableLiveData<String?>(null)

    private var existingCategories: List<StreakCategoryItem> = emptyList()

    init {
        viewModelScope.launch {
            existingCategories = streakRepository.getAllCategories()
        }
    }

    fun onCategoryNameChanged(name: String) {
        categoryName = name
    }

    private fun validateFields() {
        if (categoryName?.isNullOrEmpty() == true) {
            errorLiveData.value = EMPTY_NAME
            stateLiveData.value = CategoryCreationState.IDLE
            return
        }

        if (existingCategories.any { it.name.lowercase().equals(categoryName.lowercase()) }) {
            errorLiveData.value = EXISTING_NAME
            stateLiveData.value = CategoryCreationState.IDLE
            return
        }

        errorLiveData.value = null
        stateLiveData.value = CategoryCreationState.READY
    }

    fun onFinish() {
        if (stateLiveData.value != CategoryCreationState.READY) return

        // create category
        viewModelScope.launch {
            try {
                state = CategoryCreationState.LOADING
                streakUseCase.createCategory(CategoryRequest(categoryName))
                categoryName = ""
                state = CategoryCreationState.DONE
            } catch (ex: Throwable) {
                errorLiveData.value = NETWORK
            }
        }
    }

    fun resetData() {
        categoryName = ""
    }
}