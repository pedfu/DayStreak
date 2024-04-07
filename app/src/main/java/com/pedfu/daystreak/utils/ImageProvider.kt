package com.pedfu.daystreak.utils

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.pedfu.daystreak.R

object ImageProvider {
    fun loadImageFromUrl(imageView: ShapeableImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_gray)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}