package com.am_developer.ammarket.models

class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val cpf: Long = 0,
    val profileCompleted: Int = 0
)