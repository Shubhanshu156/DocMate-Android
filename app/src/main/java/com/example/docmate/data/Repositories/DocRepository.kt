package com.example.docmate.data.Repositories

import com.example.docmate.Service.APIService
import com.example.docmate.Service.AppointMentStatus
import com.example.docmate.data.models.Response.DocAppointmentItem
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.DoctorRequest
import retrofit2.Response
import javax.inject.Inject

class DocRepository @Inject constructor(private val ApiService:APIService) {

suspend fun getDoctorAppointment(): Response<List<DocAppointmentItem>> {
    return ApiService.getDoctorAppointment()
}
    suspend fun AcceptAppointment(appointmentid:String){
        return ApiService.AcceptAppointment(AppointMentStatus(appointmentid))
    }
    suspend fun RejectAppointment(appointmentid:String){
        return ApiService.RejectAppointment(AppointMentStatus(appointmentid))
    }
    suspend fun getDoctor(id:String): Response<Doctor> {
        return ApiService.getDoctorbyId(id)
    }

    suspend fun updateDoctorProfile(doctor: DoctorRequest) {
        return ApiService.updateDoctorProfile(doctor)
    }
}