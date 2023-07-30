package com.example.docmate.ui.Screens.Patient.Home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat.getCategory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val homeRepository: HomeRepositry,private val token: StoredToken) : ViewModel() {
    val category = mutableStateListOf<CategoryResponse>()
    val errorMessage = mutableStateOf("")
    val errorMessage2 = mutableStateOf("")
    val topDoctors = mutableStateListOf<Doctor>()
    val userProfileState = MutableStateFlow<Patient>(Patient())
    init {
        viewModelScope.launch {
            try {
                fetchCategories()
                getTopDoctors()
            } catch (e: Exception) {
                errorMessage.value = "An error occurred: ${e.message}"
            }
        }
        viewModelScope.launch {
            getPatientDetails()
        }
    }

    private suspend fun fetchCategories() {
        val res = homeRepository.getCategories()
        if (res.isSuccessful) {
            errorMessage.value = ""
            category.addAll(res.body() ?: emptyList())
        } else {
            errorMessage.value = "There seems to be an error"
        }
    }

    private suspend fun getTopDoctors() {
        val res = homeRepository.getTopDoctors()
        if (res.isSuccessful) {
            errorMessage2.value = ""
            topDoctors.addAll(res.body() ?: emptyList())
        } else {
            errorMessage2.value = "There seems to be an error"
        }
    }
    suspend fun getPatientDetails() {
        val userIDFlow = token.getUserID()
        userIDFlow.collect { userId ->

            if (userId != null) {
                val res: Response<Patient> = homeRepository.getPatientDetails(userId)
                if (res.isSuccessful && res.body() != null) {
                    userProfileState.value = res.body()!!

                }
            } else {
                Log.e("SettingViewModel", "getPatientDetails: user not loggedin")
            }
        }
    }

}
