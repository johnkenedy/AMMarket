package com.am_developer.ammarket.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val title: String = "",
    val old_price: String = "",
    val price: String = "",
    val stock_quantity: String = "",
    val unit: String = "",
    val list: String = "",
    val category: String = "",
    val image: String = "",
    var product_id: String = ""
) : Parcelable
