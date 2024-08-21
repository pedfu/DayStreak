package com.pedfu.daystreak.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pedfu.daystreak.databinding.ItemNotificationBinding
import com.pedfu.daystreak.domain.notification.NotificationItem
import com.pedfu.daystreak.presentation.home.viewholders.NotificationViewHolder

class NotificationAdapter(
    private val onClick: (notificationId: Long) -> Unit,
    private val onConfirm: (notificationId: Long) -> Unit,
    private val onDecline: (notificationId: Long) -> Unit,
) : Adapter<NotificationViewHolder>() {
    var items: List<NotificationItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notificationItem = items[position]

        if (notificationItem.type == "confirm") {
            holder.bind(notificationItem, onConfirm, onDecline, onClick)
        } else {
            holder.bind(notificationItem, onClick)
        }
    }

    override fun getItemCount(): Int = items.size
}