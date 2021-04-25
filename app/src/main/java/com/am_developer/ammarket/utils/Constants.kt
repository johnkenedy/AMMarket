package com.am_developer.ammarket.utils

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment

object Constants {
    const val USERS: String = "users"

    const val AM_PREFERENCES: String = "AMPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val LOGGED_IN_EMAIL: String = "logged_in_email"
    const val LOGGED_IN_CPF: String = "logged_in_cpf"
    const val LOGGED_IN_MOBILE: String = "mobile"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1



    fun showImageChooser(fragment: Fragment) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fragment.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}