package com.example.docmate.data.models.Response

data class Review(
    val id: String,
    val message: String,
    val patientId: String,
    val star: Int
)