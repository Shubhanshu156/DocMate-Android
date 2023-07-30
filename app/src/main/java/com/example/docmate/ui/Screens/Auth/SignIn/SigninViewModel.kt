package com.example.docmate.ui.Screens.Auth.SignIn

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.AuthRepository
import com.example.docmate.data.Repositories.HomeRepositry
import com.example.docmate.data.models.Request.AuthRequest
import com.example.docmate.data.models.Response.SigninResponse
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class SigninViewModel @Inject constructor(
    private val AuthRepo: AuthRepository,
    private val StoredToken: StoredToken,
    private val HomeRepository:HomeRepositry
) : ViewModel() {
    val signinmsg = mutableStateOf("")
    val showsnackbar = mutableStateOf(false)
    var isFirstTime = mutableStateOf(true)
    var usertype= mutableStateOf(null)
    var isSuccess = mutableStateOf(false)
    init {
        viewModelScope.launch {
            try {
                var token:String?
                var userType = ""
                StoredToken.getToken().collect {tk->
                    token = tk
                    isFirstTime.value = token.isNullOrEmpty()
                    if (!isFirstTime.value) {
                        val fcmToken = retrieveToken()
                        Log.d("SigninViewModel Firebase Token is", ":$fcmToken ")
                        HomeRepository.sendtoken(fcmToken)
                        isSuccess.value = true
                    }
                    Log.d("Token getting", "Signin: $token")
                    Log.d("User type getting", "Signin: $userType")
                }
            } catch (e: Exception) {
                Log.d("SinginViewmodelerror", "Signin: ${e.localizedMessage} ")
            }
        }

    }

    suspend fun retrieveToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result
                continuation.resume(fcmToken)
            } else {
                val exception = task.exception
                continuation.resumeWithException(exception ?: IllegalStateException("Error retrieving FCM token"))
            }
        }
    }


    fun Signin(username: String, password: String, type: String) {

        viewModelScope.launch {
            showsnackbar.value = false
            try {
                val msg: Response<SigninResponse> = AuthRepo.SignIn(
                    AuthRequest(
                        username = username,
                        password = password,
                        type = type
                    )
                )
                if (msg.isSuccessful) {
                    signinmsg.value = "SignIn Successfully"
                    StoredToken.storeToken(token=msg.body()!!.token, userType =type.uppercase(),userid=msg.body()!!.userid!!)
                    Log.e("SigninViewmodel", "You have been SIgnin Successfully ${msg.body()} ${type.uppercase()} ", )
                    isSuccess.value = true

                } else if (msg.code() == 404) {
                    signinmsg.value = "No Such User Found"
                } else if (msg.code() == 401) {
                    signinmsg.value = "Enter Correct Password!!"
                } else {
                    signinmsg.value =
                        "There seems to be Issue on server Side We are working on that"

                }
            } catch (e: Exception) {
                Log.e("Error in viewmodel", "Signin:$e ")
            }
            showsnackbar.value = true
        }
    }

}


