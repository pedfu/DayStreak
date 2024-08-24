package com.pedfu.daystreak.presentation.creation.streak.backgroundOptions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pedfu.daystreak.databinding.ItemBackgroundImageBinding

class BackgroundOptionsAdapter(
    private val context: Context
) : Adapter<BackgroundOptionsViewHolder>() {
    var items: List<BackgroundOption> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundOptionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBackgroundImageBinding.inflate(layoutInflater, parent, false)
        return BackgroundOptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackgroundOptionsViewHolder, position: Int) {
        val bgOption = items[position]
        holder.bind(bgOption.image, context)
    }

    override fun getItemCount(): Int = items.size
}