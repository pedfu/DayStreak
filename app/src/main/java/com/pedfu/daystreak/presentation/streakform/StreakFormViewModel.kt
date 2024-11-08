package com.pedfu.daystreak.presentation.streakform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

enum class DaysOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
}

enum class Screen {
    NAME,
    GOALS_PT1,
    GOALS_PT2,
    GOALS_PT3,
    CATEGORY,
    BACKGROUND,
    DESCRIPTION,
    ALL_INFO,
}

class StreakFormViewModel : ViewModel() {
    init {

    }

    private var currentScreen: Screen = Screen.NAME
        set(value) {
            previousScreen = currentScreen
            field = value
        }
    private var previousScreen: Screen? = null

    private var name: String = ""
    private var goalDeadline: Date? = null
    private var goalDeadlineForever: Boolean = false
    private var minimumPerDayInSec: Int? = null
    private var minimumPerDayNotDefined: Boolean = false
    private var daysOfWeek: List<String> = emptyList()
    private var category: String? = null
    private var localBackgroundImageName: String? = null
    private var description: String = ""

    private var fulfilledScreens = Screen.entries.map { false } as MutableList
    private var fulfilledScreenLiveData = MutableLiveData(fulfilledScreens)

    // in fragment - on change screen
        // get previous screen
        // validate using fulfilledScreen [lastIndex]

    fun navigateScreen(nextScreen: Screen) {
        currentScreen = nextScreen
        previousScreen?.let { validateScreen(it) }
    }

    private fun validateScreen(screen: Screen) {
        when (screen) {
            Screen.NAME -> fulfilledScreens[Screen.NAME.ordinal] = name?.isNullOrEmpty() == false
            Screen.GOALS_PT1 -> fulfilledScreens[Screen.GOALS_PT1.ordinal] = goalDeadline != null || goalDeadlineForever
            Screen.GOALS_PT2 -> fulfilledScreens[Screen.GOALS_PT2.ordinal] = minimumPerDayInSec != null || minimumPerDayNotDefined
            Screen.GOALS_PT3 -> fulfilledScreens[Screen.GOALS_PT3.ordinal] = daysOfWeek.isNotEmpty()
            Screen.CATEGORY -> fulfilledScreens[Screen.CATEGORY.ordinal] = category?.isNullOrEmpty() == false
            Screen.BACKGROUND -> fulfilledScreens[Screen.BACKGROUND.ordinal] = localBackgroundImageName?.isNullOrEmpty() == false
            Screen.DESCRIPTION -> fulfilledScreens[Screen.DESCRIPTION.ordinal] = description?.isNullOrEmpty() == false
            else -> {}
        }

    }
}