package com.example.docmate.ui.Screens.Patient.Schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Appointmentlist
import com.example.docmate.data.models.UserAppointments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val StoredToken: StoredToken,
    private val HomeRepository: HomeRepositry
) : ViewModel() {
    private val _userAppointents: MutableStateFlow<List<Appointmentlist>> =
        MutableStateFlow<List<Appointmentlist>>(
            emptyList()
        )
    val userAppointments: StateFlow<List<Appointmentlist>> get() = _userAppointents

    init {
        viewModelScope.launch {
            getpatientAppointment()
        }
    }

    suspend fun getpatientAppointment() {
        try {
            val res: Response<UserAppointments> = HomeRepository.getAppointments()
            if (res.isSuccessful && res.body() != null) {
                _userAppointents.value = emptyList()
                _userAppointents.value = res.body()!!.appointmentlist
            }
        } catch (e: Exception) {

        }
    }
}