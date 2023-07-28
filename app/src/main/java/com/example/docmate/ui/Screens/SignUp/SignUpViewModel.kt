package com.example.docmate.ui.Screens.SignUp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.data.Repositories.AuthRepository
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Request.AuthRequest
import com.example.docmate.ui.theme.Screens.SignUp.Type
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthRepository,private val HomeRepoSitory:HomeRepositry) : ViewModel() {
    val signupmsg = mutableStateOf("")
    val showsnackbar = mutableStateOf(false)
    val IsAccountCreated= mutableStateOf(false)

    fun signUp(username: String, password: String, type: Type) {
        viewModelScope.launch {
            showsnackbar.value = false
            try {
                if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                    signupmsg.value = "Enter All Field"
                    showsnackbar.value = true
                    return@launch
                }

                val response = authRepo.SignUp(
                    AuthRequest(
                        username = username,
                        password = password,
                        type = type.name
                    )
                )

                if (response.isSuccessful) {

                    val signUpResponse = response.body()
                    IsAccountCreated.value=true
                    signUpResponse?.let {
                        val message = it.msg
                        signupmsg.value = message
                        Log.e("SignUpViewModel", "SignUp message: ${signupmsg.value}")
                    }
                } else {
                    // Handle error case
                    val errorBody = response.errorBody()?.string()
                    signupmsg.value = extractErrorMessage(errorBody)
                    Log.e("SignUpViewModel", "SignUp error: $errorBody")
                }

            } catch (e: Exception) {
                Log.e("Error in viewmodel", "SignIn: $e")
            }

            showsnackbar.value = true
        }
    }





    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            val errorMessage = jsonObject.getString("msg")
            errorMessage
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Error parsing error body: $e")
            "Unknown Error"
        }
    }

}

