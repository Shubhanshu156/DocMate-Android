package com.example.docmate.ui.Screens.Auth.Splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docmate.Utility.datastore.StoredToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val storedToken: StoredToken) :
    ViewModel() {
    var isFirstTime = mutableStateOf<Boolean?>(null)
    var userType = mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            try {
                var token = ""
                var type = ""
                viewModelScope.async {
                    storedToken.getToken().collect { tokenValue ->
                        token = tokenValue ?: ""
                        isFirstTime.value = tokenValue.isNullOrEmpty()
                        Log.d("Token getting", "Signin: $token")
                    }
                }
                viewModelScope.async {
                    storedToken.getUserType().collect { userTypeValue ->
                        type = userTypeValue ?: ""
                        userType.value = type
                        Log.d("User type getting", "Signin: $type")
                    }
                }

                Log.d("Token getting", "Signin: $token")
                Log.d("User type getting", "Signin: $type")
            } catch (e: Exception) {
                Log.d("SplashScreenViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }
}
