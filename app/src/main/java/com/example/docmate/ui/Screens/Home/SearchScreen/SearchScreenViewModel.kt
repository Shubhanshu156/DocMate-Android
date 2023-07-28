package com.example.docmate.ui.Screens.Home.SearchScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Request.Filter
import com.example.docmate.data.models.Response.CategoryResponse
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.Response.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val HomeRepository: HomeRepositry) :
    ViewModel() {
    var lst = mutableStateListOf<Doctor>()
    var categorylst = mutableStateListOf<CategoryResponse>()
    var errorMessage = mutableStateOf("")

    init {
        viewModelScope.launch {
            try {
                val res = HomeRepository.getALlDoctors()
                if (res.isSuccessful) {
                    lst.clear()
                    res.body()?.let { lst.addAll(it) }

                } else {
                    errorMessage.value = "There seems to be error on Backend Side ${res.code()}"
                }
                fetchCategories()
            } catch (e: Exception) {
                errorMessage.value = e.localizedMessage
            }

        }
    }

    private suspend fun fetchCategories() {
        val res = HomeRepository.getCategories()
        if (res.isSuccessful) {
            errorMessage.value = ""
            categorylst.clear()
            categorylst.addAll(res.body() ?: emptyList())
        } else {
            errorMessage.value = "There seems to be an error"
        }
    }


    suspend fun Search(name: String, categoryindex: Int) {
        try {
            var category: String?
            if (categoryindex == -1) {
                category = null
            } else {
                category = categorylst[categoryindex].category
            }
            var nam: String?
            if (name.isNullOrEmpty()) {
                nam = null
            } else {
                nam = name
            }

            val v =
                HomeRepository.getSearchedDoctor(Filter = Filter(name = nam, category = category))
            if (v.isSuccessful) {
                v.body()?.doctors?.forEach { doc->
                    Log.e("SearchScreenViewModel gender", "Search: ${doc.gender }", )
                }
                lst.clear()
                v.body()?.let { lst.addAll(it.doctors) }
            } else
                errorMessage.value = v.code().toString()
        } catch (e: Exception) {
            Log.e("SearchScreenViewModel", "Search:${e.localizedMessage} ")
        }
    }


}