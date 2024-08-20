package com.pedfu.daystreak.presentation.reusable

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class CustomSnackbar {
}

fun showErrorSnackbar(view: View, @StringRes message: Int, @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).show()
}