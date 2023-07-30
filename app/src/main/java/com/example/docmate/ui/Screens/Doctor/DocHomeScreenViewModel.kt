package com.example.docmate.ui.Screens.Doctor

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.DocRepository
import com.example.docmate.data.models.Response.DocAppointmentItem
import com.example.docmate.data.models.Response.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DocHomeScreenViewModel @Inject constructor(
    private val docRepository: DocRepository,
    private val storedToken: StoredToken
) :
    ViewModel() {
    private val _docAppointents: MutableStateFlow<List<DocAppointmentItem>> =
        MutableStateFlow<List<DocAppointmentItem>>(emptyList())
    val docappointment: StateFlow<List<DocAppointmentItem>> get() = _docAppointents
    var docid = mutableStateOf<String?>("")
    var isLoading = MutableStateFlow<Boolean>(false)
    var userProfileState = MutableStateFlow<Doctor?>(Doctor())

    init {
        viewModelScope.launch {
            getDoctorAppointments()
        }
        viewModelScope.launch {
            getDoctorDetails()
        }

    }

    suspend fun getDoctorAppointments() {
        isLoading.value = true
        try {
            Log.e("DocHomeScreenViewModel", "getDoctorAppointments: ${docid}")
            val res = docRepository.getDoctorAppointment()
            if (res.isSuccessful && res.body() != null) {
                _docAppointents.value = emptyList()
                _docAppointents.value = res.body()!!
            } else {
                Log.e("DocHomeScreevViewModel", "getDoctorAppointments: ${res.code()}")
            }
        } catch (e: Exception) {
            Log.e("DocHomeScreevViewModel", "getDoctorAppointments: ${e.localizedMessage}")
        }
        isLoading.value = false
    }

    fun Accept(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                docRepository.AcceptAppointment(id)
                getDoctorAppointments()
            } catch (e: Exception) {

            }
        }
        isLoading.value = false

    }

    fun Reject(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                docRepository.RejectAppointment(id)
                getDoctorAppointments()
            } catch (e: Exception) {

            }
            isLoading.value = false
        }
    }

    suspend fun getDoctorDetails() {
        val userIDFlow = storedToken.getUserID()
        userIDFlow.collect { userId ->

            if (userId != null) {
                val res: Response<Doctor> = docRepository.getDoctor(userId)
                if (res.isSuccessful && res != null) {
                    userProfileState.value = res.body()

                }
            } else {
                Log.e("SettingViewModel", "getPatientDetails: user not loggedin")
            }
        }
    }

}