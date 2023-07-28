package com.example.docmate.data.models.Request

data class addReview(
    val doctorid:String,
    val message: String?,
    val star:String,
)