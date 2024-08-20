package com.pedfu.daystreak.presentation.reusable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.pedfu.daystreak.R

class LoadingButton: MaterialButton {
    private class ClearedState(
        val compoundDrawablePadding: Int,
        val compoundDrawables: Array<Drawable?>,
        val gravity: Int,
        val paddings: Array<Int>,
        val text: CharSequence?
    )

    private var enabledBackgroundColor: Int = currentBackgroundColor // Add a field to store the original background color
    private var disabledBackgroundColor: Int = resources.getColor(R.color.gray_light) // Set the color you want when the button is disabled

    private var paddings: Array<Int>
        get() = arrayOf(paddingLeft, paddingTop, paddingRight, paddingBottom)
        set(value) = setPadding(
            value[0],
            value[1],
            value[2],
            value[3]
        )

    private var clearedState: ClearedState? = null

    private var isCleared
        get() = clearedState != null
        set(value) {
            if (value) clear()
            else reveal()
        }

    var isLoading: Boolean
        get() = isCleared
        set(value) {
            if (value) showProgress()
            else hideProgress()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    init {
        enabledBackgroundColor = getBackgroundColor()
        backgroundTintList = ColorStateList.valueOf(enabledBackgroundColor)
    }

    private fun getBackgroundColor(): Int {
        // Attempt to retrieve color from backgroundTintList
        backgroundTintList?.defaultColor?.let {
            return it
        }
        // If backgroundTintList is null, try to get the color from the background drawable
        (background as? ColorDrawable)?.color?.let {
            return it
        }
        // If backgroundTintList is null, try to get the color from the background drawable
        (background as? GradientDrawable)?.color?.let {
            return it.defaultColor
        }
        // Default color if neither is available
        return resources.getColor(R.color.red_font, context.theme)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        backgroundTintList = if (enabled) {
            ColorStateList.valueOf(enabledBackgroundColor) // Restore the original color
        } else {
            ColorStateList.valueOf(disabledBackgroundColor) // Change to the disabled color
        }
    }

    private val currentBackgroundColor: Int
        get() = backgroundTintList?.defaultColor ?: resources.getColor(R.color.transparent)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isCleared) {
            setMeasuredDimension(measuredWidthAndState, measuredHeightAndState)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun showProgress() {
        if (isLoading) return
        clear()
        showProgress {
            buttonText = null
            gravity = DrawableButton.GRAVITY_CENTER
            progressColor = currentTextColor
        }
    }

    fun hideProgress() {
        if (!isLoading) return
        hideProgress(null)
        reveal()
    }

    private fun clear() {
        if (isCleared) return

        clearedState = ClearedState(
            compoundDrawablePadding,
            compoundDrawables,
            gravity,
            paddings,
            text
        )

        setCompoundDrawables(null, null, null, null)
        setPadding(0, 0, 0, 0)
        compoundDrawablePadding = 0
        gravity = Gravity.CENTER
        text = null
    }

    private fun reveal() {
        val state = clearedState ?: return
        clearedState = null

        compoundDrawablePadding = state.compoundDrawablePadding
        setCompoundDrawables(state.compoundDrawables)
        gravity = state.gravity
        paddings = state.paddings
        text = state.text
    }

    private fun setCompoundDrawables(drawables: Array<Drawable?>) = setCompoundDrawables(
        drawables[0],
        drawables[1],
        drawables[2],
        drawables[3]
    )
}