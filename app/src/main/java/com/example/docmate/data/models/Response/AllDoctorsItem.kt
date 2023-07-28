package com.example.docmate.data.models.Response

import com.example.docmate.ui.theme.Screens.SignIn.Gender

data class Doctor(
    val PrevSession: Int? = 0,
    val about: String? = "",
    val age: String? = "0",
    val category: String = "",
    val fullname: String = "",
    val id: String = "",
    val payment: Int? = 0,
    val rating: Int = 0,
    val ratingArray: List<Int> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val url: String? = null,
    val username: String = "",
    val working_hour_end: Int? = 23,
    val working_hour_start: Int? = 0,
    val gender: String? = "MALE"
)




enum class Gender{
    MALE,
    FEMALE
}