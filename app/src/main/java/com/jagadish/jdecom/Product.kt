package com.jagadish.jdecom

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val image: String
) : Parcelable
