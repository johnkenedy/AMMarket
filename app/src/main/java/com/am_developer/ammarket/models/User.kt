package com.am_developer.ammarket.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val cpf: Long = 0,
    val image: String = "",
    val mobile: String = "",
    val profileCompleted: Int = 0
) : Parcelable