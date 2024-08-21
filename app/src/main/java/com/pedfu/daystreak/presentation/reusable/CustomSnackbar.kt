package com.pedfu.daystreak.presentation.reusable

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pedfu.daystreak.R

class CustomSnackbar {
}

fun showErrorSnackbar(view: View, @StringRes message: Int, @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).run {
        val color = view.context.resources.getColor(R.color.red_font)
        this.setBackgroundTint(color)

        this.setTextColor(Color.WHITE)
        show()
    }
}

fun showErrorModalSnackbar(view: View, @StringRes message: Int, @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).run {
//        val color = view.context.resources.getColor(Color.RED)
        this.setBackgroundTint(Color.RED)

        this.setTextColor(Color.WHITE)
        show()
    }
}