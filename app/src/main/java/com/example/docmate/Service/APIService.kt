package com.example.docmate.Service

import com.example.docmate.data.models.Request.Appointment
import com.example.docmate.data.models.Request.AuthRequest
import com.example.docmate.data.models.Request.Filter
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
import com.example.docmate.data.models.Response.SignUpResponse
import com.example.docmate.data.models.Response.SigninResponse
import com.example.docmate.data.models.Response.TokenResponse
import com.example.docmate.data.models.UserAppointments
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface APIService {
    @POST("signin")
    suspend fun signIn(@Body request: AuthRequest): Response<SigninResponse>

    @POST("signup") // Add the appropriate endpoint URL
    suspend fun signUp(@Body request: AuthRequest): Response<SignUpResponse>

    @GET("patient/categories")
    suspend fun getCategory(): Response<List<CategoryResponse>>

    @GET("patient/topdoctors")
    suspend fun getTopDoctors(): Response<AllDoctors>

    @POST("token")
    suspend fun sendtoken(@Body token: Token): Response<TokenResponse>

    @GET("patient/getall")
    suspend fun getAllDoctors(): Response<List<Doctor>>

    @POST("patient/search")
    suspend fun getSearchedDoctors(@Body filter: Filter): Response<SearchResponse>

    @GET("getdoctor")
    suspend fun getDoctorbyId(@Query("doctorId") patientId: String): Response<Doctor>

    @POST("patient/getslots")
    suspend fun getAvailableSlots(@Body SlotDetails: SlotsDetails): Response<List<Int>>

    @POST("patient/addreview")
    suspend fun addReview(@Body addReview: addReview)

    @POST("patient/reviews")
    suspend fun getreview(@Body request: GetReviewRequest): Response<ReviewsResponse>

    @POST("patient/book")
    suspend fun bookAppointment(@Body appointment: Appointment): Response<AppointmentResponse>

    @GET("patient/getappointments")
    suspend fun getPatientAppointments(): Response<UserAppointments>

    @GET("getpatient")
    suspend fun getPatient(@Query("patientId") patientId: String): Response<Patient>

    @Multipart
    @POST("upload")
    suspend fun UploadImage(
        @Part file: MultipartBody.Part
    ): Response<ImageResponse>

    @PATCH("patient/profile")
    suspend fun updatePatientProfile(@Body patient: Patient)
}

data class GetReviewRequest(
    val doctorid: String
)

data class ReviewResponse(
    val message: String
)
