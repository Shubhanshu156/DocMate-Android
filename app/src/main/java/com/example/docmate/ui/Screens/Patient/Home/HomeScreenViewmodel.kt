package com.example.docmate.ui.Screens.Patient.Home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat.getCategory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.data.models.Response.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val homeRepository: HomeRepositry) : ViewModel() {
    val category = mutableStateListOf<CategoryResponse>()
    val errorMessage = mutableStateOf("")
    val errorMessage2 = mutableStateOf("")
    val topDoctors = mutableStateListOf<Doctor>()

    init {
        viewModelScope.launch {
            try {
                fetchCategories()
                getTopDoctors()
            } catch (e: Exception) {
                errorMessage.value = "An error occurred: ${e.message}"
            }
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
}
