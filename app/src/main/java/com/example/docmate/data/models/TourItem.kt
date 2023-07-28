package com.example.docmate.data.models

import androidx.annotation.DrawableRes

data class TourItem(
    val description: String,
    @DrawableRes val image: Int,
    val title: String
)
