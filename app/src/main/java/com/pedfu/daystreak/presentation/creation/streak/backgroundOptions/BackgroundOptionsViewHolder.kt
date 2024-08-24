package com.pedfu.daystreak.presentation.creation.streak.backgroundOptions

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedfu.daystreak.databinding.ItemBackgroundImageBinding
import com.pedfu.daystreak.utils.ImageProvider

class BackgroundOptionsViewHolder(
    private val binding: ItemBackgroundImageBinding
) : ViewHolder(binding.root) {
    fun bind(imageRes: Int?, context: Context) = binding.run {
        if (imageRes != null) {
            val compressedImage = ImageProvider.loadCompressedImage(imageRes, context)
            image.setImageBitmap(compressedImage)
        }
    }
}