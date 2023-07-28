package com.example.docmate.data.Repositories

import android.util.Log
import com.example.docmate.Service.APIService
import com.example.docmate.data.models.Request.AuthRequest
import com.example.docmate.data.models.Response.SignUpResponse
import com.example.docmate.data.models.Response.SigninResponse
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Response
import javax.inject.Inject

@ActivityScoped
class AuthRepository @Inject constructor(private val APIService:APIService) {
    suspend fun SignIn(AuthRequest:AuthRequest): Response<SigninResponse> {
        return APIService.signIn(AuthRequest)
    }
    suspend fun SignUp(AuthRequest:AuthRequest): Response<SignUpResponse> {
       val response=APIService.signUp(AuthRequest)
        return response
    }


}