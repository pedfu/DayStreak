package com.pedfu.daystreak.presentation.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.pedfu.daystreak.R
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.presentation.home.adapters.NotificationAdapter

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

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerViewNotifications = dialog.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerViewNotifications.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewNotifications.adapter = notificationAdapter.apply {
            items = notificationItems
        }
        ViewCompat.setNestedScrollingEnabled(recyclerViewNotifications, true)

        dialog.show()
    }

    fun showConfirmationDialog(
        context: Context,
        onConfirm: () -> Unit,
        title: String,
        message: String? = null,
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textViewTitle = dialog.findViewById<TextView>(R.id.textViewTitle)
        val textViewMessage = dialog.findViewById<TextView>(R.id.textViewMessage)
        textViewTitle.text = title
        textViewMessage.text = message ?: ""
        textViewMessage.isVisible = message != null

        val buttonClose = dialog.findViewById<ImageButton>(R.id.buttonClose)
        val buttonConfirm = dialog.findViewById<ImageButton>(R.id.buttonConfirm)
        val buttonCancel = dialog.findViewById<ImageButton>(R.id.buttonCancel)
        buttonClose.setOnClickListener {
            dialog.hide()
        }
        buttonCancel.setOnClickListener {
            dialog.hide()
        }
        buttonConfirm.setOnClickListener {
            onConfirm()
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
}