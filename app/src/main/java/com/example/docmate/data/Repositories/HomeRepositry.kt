package com.example.docmate.data.Repositories

import android.util.Log
import com.example.docmate.Service.APIService
import com.example.docmate.Service.GetReviewRequest
import com.example.docmate.Service.ReviewResponse
import com.example.docmate.data.models.Request.Appointment
import com.example.docmate.data.models.Request.Filter
import com.example.docmate.data.models.Request.SearchbyId
import com.example.docmate.data.models.Request.SlotsDetails
import com.example.docmate.data.models.Request.Token
import com.example.docmate.data.models.Request.addReview
import com.example.docmate.data.models.Response.AllDoctors
import com.example.docmate.data.models.Response.AppointmentResponse
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.ImageResponse
import com.example.docmate.data.models.Response.Patient
import com.example.docmate.data.models.Response.ReviewsResponse
import com.example.docmate.data.models.Response.SearchResponse
import com.example.docmate.data.models.Response.TokenResponse
import com.example.docmate.data.models.UserAppointments
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class HomeRepositry @Inject constructor(private val APIService: APIService) {
    suspend fun BookAppointment(appointment: Appointment): Response<AppointmentResponse> {
        return APIService.bookAppointment(appointment)
    }

    suspend fun getCategories(): Response<List<CategoryResponse>> {
        return APIService.getCategory()
    }

    suspend fun getTopDoctors(): Response<AllDoctors> {
        return APIService.getTopDoctors()
    }

    suspend fun sendtoken(token: String): Response<TokenResponse> {
        try {

            return APIService.sendtoken(Token(token))
        } catch (e: Exception) {
            Log.e("FirebaseToken", "sendtoken:${e.localizedMessage} ")
            throw Exception(e.localizedMessage)
        }
    }

    suspend fun getSearchedDoctor(Filter: Filter): Response<SearchResponse> {
        return APIService.getSearchedDoctors(Filter)
    }
    suspend fun getALlDoctors(): Response<List<Doctor>> {
        return APIService.getAllDoctors()
    }
    suspend fun getDoctorbyId(id:String): Response<Doctor> {
        return APIService.getDoctorbyId(id)
    }
    suspend fun getAvailableSlots(id:String,month:Int,year:Int,date:Int):Response<List<Int>>{
        return APIService.getAvailableSlots(SlotsDetails(id=id,month=month,year=year,date=date))
    }
    suspend fun addreview(addReview: addReview) {
        APIService.addReview(addReview)
    }

    suspend fun getReviews(id:String): Response<ReviewsResponse> {
        return APIService.getreview(GetReviewRequest(id))
    }
    suspend fun getAppointments(): Response<UserAppointments> {
        return APIService.getPatientAppointments()
    }
    suspend fun getPatientDetails(patientid:String): Response<Patient> {
        return APIService.getPatient(patientid)
    }

    suspend  fun uploadImage(profileurl: MultipartBody.Part): Response<ImageResponse> {
        return APIService.UploadImage(profileurl)
    }

    suspend fun updatePatientProfile(Patient:Patient) {
        return APIService.updatePatientProfile(Patient)
    }
}