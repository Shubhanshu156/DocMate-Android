package com.example.docmate.data.models

data class Appointmentlist(
    val date: Int,
    val doctorId: String,
    val doctorname: String,
    val durationMinutes: Int,
    val id: String,
    val month: Int,
    val patientId: String,
    val patientname: String,
    val status: String,
    val time: Int,
    val url: String,
    val year: Int
)