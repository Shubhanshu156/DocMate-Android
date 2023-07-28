package com.example.docmate.ui.Screens.SettingScreen

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.Resource
import com.example.docmate.Utility.Status
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Response.ImageResponse
import com.example.docmate.data.models.Response.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel

class SettingScreenViewModel @Inject constructor(
    private val token: StoredToken,
    private val homeRepository: HomeRepositry
) :
    ViewModel() {
    val userProfileState = MutableStateFlow<Patient?>(null)
    val imageurl = MutableStateFlow<String?>(null)
    var ProfileStatus=MutableStateFlow(Status.NotCalled)
    init {
        viewModelScope.launch {
            getPatientDetails()
        }
    }


    suspend fun getPatientDetails() {
        val userIDFlow = token.getUserID()
        userIDFlow.collect { userId ->

            if (userId != null) {
                val res: Response<Patient> = homeRepository.getPatientDetails(userId)
                if (res.isSuccessful && res != null) {
                    userProfileState.value = res.body()

                }
            } else {
                Log.e("SettingViewModel", "getPatientDetails: user not loggedin")
            }
        }
    }

    fun savedetails(
        email: String?, mobilenumber: String?,
        address: String?,
        age: String?,
        gender: String?,
        name: String?,
        profileurl: MultipartBody.Part?
    ) {

        viewModelScope.launch {
            ProfileStatus.value=Status.Loading
            try {

                if (profileurl != null) {
                    val uploadres: Response<ImageResponse> = uploadimage(profileurl)
                    if (uploadres.isSuccessful && uploadres != null) {
                        imageurl.value = uploadres.body()?.URL
                    }

                }
                homeRepository.updatePatientProfile(
                    Patient(
                        email = email,
                        contactNumber = mobilenumber,
                        address = address,
                        gender = gender,
                        name = name,
                        age = age?.toIntOrNull(),
                                profileurl = imageurl?.value ?: userProfileState.value?.profileurl
                    )
                )
                delay(1000)
                ProfileStatus.value=Status.Success
            } catch (e: Exception) {
                Log.e("SettingScreenViewmodel", "savedetails: ${e.localizedMessage} $email,$mobilenumber,$address,$gender,$name,$age,$profileurl")
            }
        }
    }


    suspend fun uploadimage(profileurl: MultipartBody.Part): Response<ImageResponse> {
        return homeRepository.uploadImage(profileurl)
    }
}
