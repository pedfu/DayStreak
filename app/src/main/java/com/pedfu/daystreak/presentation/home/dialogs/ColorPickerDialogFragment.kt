package com.pedfu.daystreak.presentation.home.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ColorPickerDialogFragment
//    : DialogFragment()
{
    private var listener: OnColorSelectedListener? = null

    fun setOnColorSelectedListener(listener: OnColorSelectedListener) {
        this.listener = listener
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//    }
}