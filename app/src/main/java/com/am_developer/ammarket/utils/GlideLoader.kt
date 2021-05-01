package com.am_developer.ammarket.utils

import android.content.Context
import android.widget.ImageView
import com.am_developer.ammarket.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader (val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.profile_holder)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}