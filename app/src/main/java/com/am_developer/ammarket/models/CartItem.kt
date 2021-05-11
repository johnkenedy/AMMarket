package com.am_developer.ammarket.models

data class CartItem (
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    val cart_quantity: String = "",
    var stock_quantity: String = "",
    var id: String = ""
)