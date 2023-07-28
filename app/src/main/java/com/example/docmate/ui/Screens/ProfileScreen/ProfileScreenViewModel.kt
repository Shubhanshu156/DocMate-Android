package com.example.docmate.ui.Screens.ProfileScreen

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Request.Appointment
import com.example.docmate.data.models.Request.addReview
import com.example.docmate.data.models.Response.Doctor
import com.example.docmate.data.models.ReviewItem
import com.example.docmate.ui.graphs.HomeScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val HomeRepostory: HomeRepositry) :
    ViewModel() {
    var doctor = mutableStateOf(Doctor())
    private val _isloading= MutableStateFlow(false)
    val isloading get()=_isloading
    var reviewstatus= mutableStateOf("")
    val errormsg= mutableStateOf("")
    private val _availableSlots = MutableStateFlow<List<Pair<Int, Int>>?>(null)
    val availableSlots: StateFlow<List<Pair<Int, Int>>?> get() = _availableSlots
    private val _reviews= MutableStateFlow<List<ReviewItem>>(emptyList())
    val reviews: StateFlow<List<ReviewItem>> get() = _reviews
     val _appointmentstatus= MutableStateFlow<Boolean>(false)



    fun getDoctor(id: String?) {
        if (id != null) {
            viewModelScope.launch {
                try {
                    val response = HomeRepostory.getDoctorbyId(id)
                    if (response.isSuccessful && response != null) {
                        doctor.value = response.body()!!
                    } else {
                        throw Exception(response.code().toString())
                    }
                } catch (e: java.lang.Exception) {
                    throw Exception(e.localizedMessage)
                }
            }
        }
    }
    fun getAvailableSlots(id:String,month:Int, year:Int, date:Int){
        try {
            viewModelScope.launch {
                _isloading.value = true
                _availableSlots.value = emptyList()
                delay(1000)
                val res = HomeRepostory.getAvailableSlots(id = id, month = month, year = year, date = date)
                if (res.isSuccessful && res.body() != null) {
                    val availableSlotsList = mutableListOf<Pair<Int, Int>>()
                    res.body()!!.forEach { ele ->
                        availableSlotsList.add(Pair(ele, ele + 1))
                    }
                    _availableSlots.emit(availableSlotsList) // Emit the new list to update the StateFlow
                } else {
                    Log.e("ProfileScreenViewModel", "getAvailableSlots:${res.code()} ")
                    errormsg.value = "There seems to be some issue"
                }
                _isloading.value = false
            }
        } catch (e: Exception) {
            Log.e("ProfileScreenViewModel", "getAvailableSlots:${e.localizedMessage} ")
            _isloading.value = false
        }

    }
    fun addreview(id:String,message:String,star:Int){
        try{
            viewModelScope.launch {
                val reviews=addReview(id,message,star.toString())
                val res=HomeRepostory.addreview(reviews)
                getReviews(id)

            }

        }
        catch (e:Exception){
            reviewstatus.value=e.localizedMessage
        }
    }

    fun getReviews(id:String){
        try{
            viewModelScope.launch {
                val res=HomeRepostory.getReviews(id)
                if(res.isSuccessful){
                 _reviews.value= emptyList<ReviewItem>()
                 var temp= res.body()?.reviews?.map {
                     ReviewItem(patientname = "DocMate User", patientmessage = it.message, patientstar = it.star.toString())
                 }
                    if (temp != null) {
                        _reviews.value=temp
                    }
                }
                else{
                    Log.e("profilescreenviewmodel", "getReviews: $res", )
                }

            }

        }
        catch (e:Exception){
            Log.e("profilescreenviewmodel", "getReviews: ${e.localizedMessage}", )

        }
    }

    fun bookAppointment(doctorid:String,year:Int,month:Int,date:Int,time:Int,url:String){
        try {
            viewModelScope.launch {
                val res=HomeRepostory.BookAppointment(Appointment(doctorid=doctorid,year=year,month=month,date=date,time=time,url=url))
                _appointmentstatus.value = res.isSuccessful
            }
        }
        catch (e:Exception){
            _appointmentstatus.value=false
            Log.e("ProfileScreenViewModel", "bookAppointment: ${e.localizedMessage}", )

        }
    }

    }
