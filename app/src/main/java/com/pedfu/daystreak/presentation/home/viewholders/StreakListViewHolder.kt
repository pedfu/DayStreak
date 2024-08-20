package com.pedfu.daystreak.presentation.home.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedfu.daystreak.R
import com.pedfu.daystreak.databinding.ItemCardBinding
import com.pedfu.daystreak.domain.streak.StreakItem
import com.pedfu.daystreak.domain.streak.StreakStatus
import com.pedfu.daystreak.utils.ImageProvider

class StreakListViewHolder(
    private val binding: ItemCardBinding,
) : ViewHolder(binding.root) {
    fun bind(streakItem: StreakItem, onClick: (streakId: Long) -> Unit) = binding.run {
        setupCard(streakItem, onClick)
        setupTag(streakItem)
        setupText(streakItem)
    }

    private fun ItemCardBinding.setupCard(streakItem: StreakItem, onClick: (streakId: Long) -> Unit) {
        if (streakItem.backgroundPicture != null) ImageProvider.loadImageFromUrl(imageViewCardBg, streakItem.backgroundPicture)
        contraintLayoutCard.setOnClickListener {
            onClick(streakItem.id ?: 0)
        }
    }

    private fun ItemCardBinding.setupTag(streakItem: StreakItem) {
        imageViewCardTag.setImageDrawable(
            ContextCompat.getDrawable(
                this.root.context,
                when (streakItem.status) {
                    StreakStatus.PENDING -> R.drawable.ic_bell
                    StreakStatus.STREAK_OVER -> R.drawable.ic_pencil
                    StreakStatus.DAY_DONE -> R.drawable.ic_check
                }
            )
        )
        textViewCardTag.text = this.root.context.getString(when(streakItem.status) {
            StreakStatus.PENDING -> R.string.pending
            StreakStatus.STREAK_OVER -> R.string.over
            StreakStatus.DAY_DONE -> R.string.done
        })
        cardTag.background = ContextCompat.getDrawable(
            this.root.context,
            when (streakItem.status) {
                StreakStatus.PENDING -> R.drawable.background_rounded_blue
                StreakStatus.STREAK_OVER -> R.drawable.background_rounded_red
                StreakStatus.DAY_DONE -> R.drawable.background_rounded_green
            }
        )
    }

    private fun ItemCardBinding.setupText(streakItem: StreakItem) {
        textViewTitle.text = streakItem.name
        textViewDescription.text = streakItem.description ?: ""
    }
}