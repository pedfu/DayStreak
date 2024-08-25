package com.pedfu.daystreak.presentation.creation.streak.backgroundOptions

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedfu.daystreak.databinding.ItemBackgroundImageBinding
import com.pedfu.daystreak.utils.ImageProvider

class BackgroundOptionsViewHolder(
    private val binding: ItemBackgroundImageBinding,
    private val onSelectImage: (String, Int) -> Unit
) : ViewHolder(binding.root) {
    fun bind(imageRes: Int?, name: String) = binding.run {
        if (imageRes != null) {
            image.setImageResource(imageRes)
            image.setOnClickListener {
                if (imageRes != null) onSelectImage(name,  imageRes)
            }
        }
    }
}