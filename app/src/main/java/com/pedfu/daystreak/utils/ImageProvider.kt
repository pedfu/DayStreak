package com.pedfu.daystreak.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.pedfu.daystreak.R
import java.io.ByteArrayOutputStream

object ImageProvider {
    fun loadImageFromUrl(imageView: ShapeableImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .error(R.drawable.img_error_loading_image)
            .placeholder(R.drawable.img_default_user)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    fun loadLocalImage(imageName: String, context: Context): Int {
        val resourceName = imageName.substringBeforeLast(".")
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    fun loadOptimizedLocalImage(imageName: String, context: Context): Int {
        val resourceName = imageName.substringBeforeLast(".") + "_optimized_50"
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    fun compressBitmap(bitmap: Bitmap, quality: Int = 80): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}

val BACKGROUND_OPTIONS = listOf(
    "bg_balloons.jpg",
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