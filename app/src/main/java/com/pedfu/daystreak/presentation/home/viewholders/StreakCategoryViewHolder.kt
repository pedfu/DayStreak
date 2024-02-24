package com.pedfu.daystreak.presentation.home.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.ItemStreakCategoryBinding
import com.pedfu.daystreak.domain.streak.StreakCategoryItem

class StreakCategoryViewHolder(
    private val binding: ItemStreakCategoryBinding,
) : ViewHolder(binding.root) {
    fun bind(streakCategoryItem: StreakCategoryItem, onClick: (categoryId: Long) -> Unit) = binding.run {
        setupText(streakCategoryItem)

        contraintLayoutCard.setOnClickListener {
            onClick(streakCategoryItem.id)
        }
    }

    private fun ItemStreakCategoryBinding.setupText(streakCategoryItem: StreakCategoryItem) {
        mainText.text = streakCategoryItem.name

        if (streakCategoryItem.selected) {
            mainText.background = ContextCompat.getDrawable(
                this.root.context,
                R.drawable.background_rounded_yellow
            )
        } else {
            mainText.setBackgroundColor(ContextCompat.getColor(root.context, R.color.transparent))
        }
    }
}