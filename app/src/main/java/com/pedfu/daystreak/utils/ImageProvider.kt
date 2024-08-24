package com.pedfu.daystreak.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.pedfu.daystreak.R
import com.pedfu.daystreak.presentation.creation.streak.backgroundOptions.BackgroundOption

object ImageProvider {
    fun loadImageFromUrl(imageView: ShapeableImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_gray)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    fun loadLocalImage(imageName: String, context: Context): Int {
        val resourceName = imageName.substringBeforeLast(".")
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    fun loadCompressedImage(resourceId: Int, context: Context): Bitmap {
        val options = BitmapFactory.Options().apply {
            inSampleSize = 20
            inPreferredConfig = Bitmap.Config.RGB_565
        }
        return BitmapFactory.decodeResource(context.resources, resourceId, options)
    }
}

val BACKGROUND_OPTIONS = listOf(
    "bg_background.jpg",
    "bg_background2.jpg",
    "bg_balloon.jpg",
    "bg_books.jpg",
    "bg_car.jpg",
    "bg_car2.jpg",
    "bg_clothes.jpg",
    "bg_fancycar.jpg",
    "bg_food.jpg",
    "bg_food2.jpg",
    "bg_houses.jpg",
    "bg_landscape.jpg",
    "bg_manycars.jpg",
    "bg_motorcycle.jpg",
    "bg_park.jpg",
    "bg_pencils.jpg",
    "bg_roses.jpg",
    "bg_sport.jpg",
    "bg_windows.jpg",
    "bg_work.jpg",
)