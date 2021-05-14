package com.am_developer.ammarket.utils

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment

object Constants {
    const val USERS: String = "users"
    const val USER_ID: String = "user_id"
    const val PRODUCTS: String = "products"
    const val CATEGORY: String = "category"
    const val ADDRESSES: String = "addresses"
    const val LIST: String = "list"

    const val AM_PREFERENCES: String = "AMPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val LOGGED_IN_EMAIL: String = "logged_in_email"
    const val LOGGED_IN_CPF: String = "logged_in_cpf"
    const val LOGGED_IN_MOBILE: String = "mobile"
    const val IMAGE: String = "image"
    const val USER_PROFILE_IMAGE: String = "User_profile_Image"

    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_CATEGORY: String = "extra_product_category"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_ITEMS: String = "cart_items"
    const val PRODUCT_ID: String = "product_id"
    const val CART_QUANTITY: String = "cart_quantity"

    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"



    fun showImageChooser(fragment: Fragment) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fragment.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(fragment: Fragment, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(fragment.context?.contentResolver?.getType(uri!!))
    }

}