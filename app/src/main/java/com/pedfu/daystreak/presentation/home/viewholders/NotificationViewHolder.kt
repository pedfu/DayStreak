package com.pedfu.daystreak.presentation.home.viewholders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedfu.daystreak.databinding.ItemNotificationBinding
import com.pedfu.daystreak.domain.notification.NotificationItem

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
) : ViewHolder(binding.root) {
    fun bind(notificationItem: NotificationItem, onClick: (notificationId: Long) -> Unit) = binding.run {
        setupCard(notificationItem, onClick)
        setupCard(notificationItem)
        setupText(notificationItem)
    }
    fun bind(notificationItem: NotificationItem, onConfirm: (notificationId: Long) -> Unit, onDecline: (notificationId: Long) -> Unit, onClick: (notificationId: Long) -> Unit) = binding.run {
        setupText(notificationItem)
        setupCard(notificationItem, onConfirm, onDecline, onClick)
        setupCard(notificationItem)
    }

    private fun ItemNotificationBinding.setupCard(notificationItem: NotificationItem) {
        linearLayoutButtons.isVisible = notificationItem.type == "confirm"
        imageViewNotRead.isVisible = !notificationItem.read
    }

    private fun ItemNotificationBinding.setupCard(notificationItem: NotificationItem, onClick: (streakId: Long) -> Unit) {
        contraintLayoutItem.setOnClickListener {
            onClick(notificationItem.id)
        }
    }
    private fun ItemNotificationBinding.setupCard(notificationItem: NotificationItem, onConfirm: (notificationId: Long) -> Unit, onDecline: (notificationId: Long) -> Unit, onClick: (streakId: Long) -> Unit) {
        buttonAccept.setOnClickListener { onConfirm(notificationItem.id) }
        buttonDecline.setOnClickListener { onDecline(notificationItem.id) }
        contraintLayoutItem.setOnClickListener {
            onClick(notificationItem.id)
        }
    }

    private fun ItemNotificationBinding.setupText(notificationItem: NotificationItem) {
        textViewPrimary.text = notificationItem.message
    }
}