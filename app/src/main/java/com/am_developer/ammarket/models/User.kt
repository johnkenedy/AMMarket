package com.am_developer.ammarket.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val profileCompleted: Int = 0
)