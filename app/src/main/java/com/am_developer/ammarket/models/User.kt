package com.am_developer.ammarket.models

class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val cpf: Long = 0,
    val image: String = "",
    val mobile: String = "",
    val profileCompleted: Int = 0
)