package com.pedfu.daystreak.utils

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.pedfu.daystreak.R
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter
import java.util.Date

object Modals {
    fun showNotificationDialog(
        context: Context,
        notificationItems: List<NotificationItem>,
        notificationAdapter: NotificationAdapter
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_notifications)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val recyclerViewNotifications = dialog.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerViewNotifications.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewNotifications.adapter = notificationAdapter.apply {
            items = notificationItems
        }

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonClear = dialog.findViewById<MaterialButton>(R.id.buttonClear)
        val buttonMarkAllRead = dialog.findViewById<MaterialButton>(R.id.buttonMarkAllRead)
        val textViewNoItems = dialog.findViewById<TextView>(R.id.textViewNoItems)

        buttonClose.setOnClickListener { dialog.dismiss() }
        buttonClear.setOnClickListener { dialog.dismiss() }
        if (notificationItems.isEmpty()) {
            buttonClear.setTextAppearance(R.style.MaterialButton_Transparent_Disabled)
            buttonClear.isEnabled = false
            buttonMarkAllRead.setTextAppearance(R.style.MaterialButton_Disabled)
            buttonMarkAllRead.isEnabled = false
            textViewNoItems.isVisible = true
        }

        ViewCompat.setNestedScrollingEnabled(recyclerViewNotifications, true)
        dialog.show()
    }

    fun showConfirmationDialog(
        context: Context,
        onConfirm: () -> Unit,
        title: String,
        message: String? = null,
        onCancel: (() -> Unit)? = null,
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val textViewTitle = dialog.findViewById<TextView>(R.id.textViewTitle)
        val textViewMessage = dialog.findViewById<TextView>(R.id.textViewMessage)
        textViewTitle.text = title
        textViewMessage.text = message ?: ""
        textViewMessage.isVisible = message != null

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonConfirm = dialog.findViewById<MaterialButton>(R.id.buttonConfirm)
        val buttonCancel = dialog.findViewById<MaterialButton>(R.id.buttonCancel)
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonCancel.setOnClickListener {
            dialog.hide()
//            if (onCancel != null) {
//                onCancel()
//            }
        }
        buttonConfirm.setOnClickListener {
            onConfirm()
        }

        dialog.show()
    }

    fun showConfirmationAdvancedDialog(
        context: Context,
        onClick: (option: Int, dialog: Dialog) -> Unit,
        title: String,
        firstOptionText: String,
        secondOptionText: String,
        thirdOptionText: String,
        message: String? = null,
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_confirm_advanced)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val textViewTitle = dialog.findViewById<TextView>(R.id.textViewTitle)
        val textViewMessage = dialog.findViewById<TextView>(R.id.textViewMessage)
        textViewTitle.text = title
        textViewMessage.text = message ?: ""
        textViewMessage.isVisible = message != null

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonFirstOption = dialog.findViewById<MaterialButton>(R.id.buttonFirstOption)
        buttonFirstOption.text = firstOptionText
        val buttonSecondOption = dialog.findViewById<MaterialButton>(R.id.buttonSecondOption)
        buttonSecondOption.text = secondOptionText
        val buttonThirdOption = dialog.findViewById<MaterialButton>(R.id.buttonThirdOption)
        buttonThirdOption.text = thirdOptionText
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonFirstOption.setOnClickListener {
            onClick(1, dialog)
        }
        buttonSecondOption.setOnClickListener {
            onClick(2, dialog)
        }
        buttonThirdOption.setOnClickListener {
            onClick(3, dialog)
        }

        dialog.show()
    }

    fun showBadgeDialog(
        context: Context,
        onShareClick: () -> Unit,
        name: String,
        description: String,
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_new_badge)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val textViewBadgeName = dialog.findViewById<TextView>(R.id.textViewBadgeName)
        val textViewMessage = dialog.findViewById<TextView>(R.id.textViewMessage)
        textViewBadgeName.text = name
        textViewMessage.text = description

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonShare = dialog.findViewById<MaterialButton>(R.id.buttonShare)
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonShare.setOnClickListener {
            onShareClick()
        }

        dialog.show()
    }

    fun showCompleteDayDialog(
        context: Context,
        onSave: () -> Unit,
        startHour: Date? = null,
        endHour: Date? = null,
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_complete_day)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val dateLabel = dialog.findViewById<TextView>(R.id.labelDate)
        val textInputDate = dialog.findViewById<TextInputLayout>(R.id.textInputDate)

        val todayTab = dialog.findViewById<TextView>(R.id.today)
        val anotherDayTab = dialog.findViewById<LinearLayout>(R.id.anotherDay)
        todayTab.setOnClickListener {
            todayTab.background = ContextCompat.getDrawable(context, R.drawable.background_border_bottom_light)
            anotherDayTab.background = null
            dateLabel.isVisible = false
            textInputDate.isVisible = false
        }
        anotherDayTab.setOnClickListener {
            anotherDayTab.background = ContextCompat.getDrawable(context, R.drawable.background_border_bottom_light)
            todayTab.background = null
            dateLabel.isVisible = true
            textInputDate.isVisible = true
        }

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonSave = dialog.findViewById<MaterialButton>(R.id.buttonSave)
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonSave.setOnClickListener {
//            onSave(startHour, endHour, date, description)
            onSave()
        }

        dialog.show()
    }
}