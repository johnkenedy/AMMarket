package com.am_developer.ammarket.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount : String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    val payment_mode: String = "",
    val how_much_change: String = "",
    var shipping_progress: String = "",
    var id: String = ""
        ) : Parcelable