package com.am_developer.ammarket.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.am_developer.ammarket.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader (val context: Context) {

    fun loadUserPicture(imageURI: Uri, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(Uri.parse(imageURI.toString()))
                .centerCrop()
                .placeholder(R.drawable.profile_holder)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}