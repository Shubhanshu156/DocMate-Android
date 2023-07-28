package com.example.docmate.data.models.Response



data class AppointmentResponse(
    val id:String,
    val patientId: String,
    val doctorId: String,
    val date: Int,
    val month:Int,
    val year:Int,
    val time:Int,
    val durationMinutes: Int,
    var status: AppointmentStatus,
    val url: String?
)

enum class AppointmentStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED,
    COMPLETED
}
