package com.pedfu.daystreak.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pedfu.daystreak.databinding.ItemStreakCategoryBinding
import com.pedfu.daystreak.domain.streak.StreakCategoryItem
import com.pedfu.daystreak.presentation.home.viewholders.StreakCategoryViewHolder

class StreakCategoryAdapter(
    private val onClick: (streakId: Long) -> Unit
) : Adapter<StreakCategoryViewHolder>() {
    var items: List<StreakCategoryItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreakCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStreakCategoryBinding.inflate(layoutInflater, parent, false)
        return StreakCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StreakCategoryViewHolder, position: Int) {
        val streakCategoryItem = items[position]
        holder.bind(streakCategoryItem, onClick)
    }

    override fun getItemCount(): Int = items.size


}