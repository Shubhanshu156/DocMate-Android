package com.example.docmate.data.models.Response

import com.example.docmate.ui.theme.Screens.SignIn.Gender

data class Doctor
    (
    val PrevSession: Int? = 0,
    val about: String? = "",
    val age: String? = "0",
    val category: String? = "",
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


data class DoctorRequest(
    val username:String?=null,
    val category:String?=null,
    val fullname:String?=null,
    val age:String?=null,
    val about:String?=null,
    val payment:Int?=null,
    val working_hour_start:Int?=null,
    val working_hour_end: Int?=null,
    val profileurl:String?=null,
    val gender: String?=null
)

enum class Gender{
    MALE,
    FEMALE
}