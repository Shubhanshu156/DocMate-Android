package com.example.docmate.ui.Screens.Doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.Status
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.DocRepository
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.DoctorRequest
import com.example.docmate.data.models.Response.ImageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DocProfileViewModel @Inject constructor(
    private val DocRepository: DocRepository,
    private val token: StoredToken,
    private val homeRepositry: HomeRepositry
) : ViewModel() {
    val userProfileState = MutableStateFlow<Doctor?>(null)
    val imageurl = MutableStateFlow<String?>(null)
    var ProfileStatus = MutableStateFlow(Status.NotCalled)
    private var _categorylst = MutableStateFlow<List<CategoryResponse>>(emptyList())
    val categorylst: StateFlow<List<CategoryResponse>> = _categorylst

    init {
        try {

            viewModelScope.launch { getDoctorDetails() }
            viewModelScope.launch { getCategory() }


        } catch (e: Exception) {
            Log.e("DocProfileVIewmodel", "${e.localizedMessage} ")
        }
    }

    suspend fun getDoctorDetails() {
        val userIDFlow = token.getUserID()
        userIDFlow.collect { userId ->

            if (userId != null) {
                val res: Response<Doctor> = DocRepository.getDoctor(userId)
                if (res.isSuccessful && res != null) {
                    userProfileState.value = res.body()

                }
            } else {
                Log.e("SettingViewModel", "getPatientDetails: user not loggedin")
            }
        }
    }

    fun savedetails(
        workinghourstart: String?,
        workinghourend: String?,
        category: String?,
        about: String?,
        age: String?,
        gender: String?,
        name: String?,
        profileurl: MultipartBody.Part?
    ) {

        viewModelScope.launch {
            ProfileStatus.value = Status.Loading
            try {

                if (profileurl != null) {
                    val uploadres: Response<ImageResponse> = uploadimage(profileurl)
                    if (uploadres.isSuccessful && uploadres != null) {
                        imageurl.value = uploadres.body()?.URL
                    }

                }
                DocRepository.updateDoctorProfile(
                    DoctorRequest(
                        category = category,
                        fullname = name,
                        age = age,
                        about = about,
                        working_hour_end = workinghourend?.toIntOrNull(),
                        working_hour_start = workinghourstart?.toIntOrNull(),
                        profileurl = imageurl.value

                    )
                )
                delay(1000)
                ProfileStatus.value = Status.Success
            } catch (e: Exception) {
                Log.e(
                    "SettingScreenViewmodel",
                    "savedetails: ${e.localizedMessage}$gender,$name,$age,$profileurl"
                )
            }
        }
    }

    suspend fun uploadimage(profileurl: MultipartBody.Part): Response<ImageResponse> {
        return homeRepositry.uploadImage(profileurl)
    }

    suspend fun getCategory() {
        try {
            val res = homeRepositry.getCategories()
            if (res.isSuccessful && res.body() != null) {
                _categorylst.value = res.body()!!
            }
        } catch (e: Exception) {
            Log.e("DocProfileViewmodel", "getCategory: ${e.localizedMessage}")
        }

    }
}