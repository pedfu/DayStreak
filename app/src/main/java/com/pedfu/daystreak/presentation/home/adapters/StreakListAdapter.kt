package com.pedfu.daystreak.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pedfu.daystreak.databinding.ItemCardBinding
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.presentation.home.dialogs.confirmDeleteCategory.ConfirmDeleteType
import com.pedfu.daystreak.presentation.home.viewholders.StreakListViewHolder

class StreakListAdapter(
    private val onClick: (streakId: Long) -> Unit,
    private val showPopupMenu: (View, Long, ConfirmDeleteType) -> Unit,
) : Adapter<StreakListViewHolder>() {
    var items: List<StreakItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreakListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(layoutInflater, parent, false)
        return StreakListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StreakListViewHolder, position: Int) {
        val streakItem = items[position]
        holder.bind(streakItem, onClick, showPopupMenu)
    }

    override fun getItemCount(): Int = items.size


}