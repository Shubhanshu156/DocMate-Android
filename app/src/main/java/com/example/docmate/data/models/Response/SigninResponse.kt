package com.example.docmate.data.models.Response

data class SigninResponse(
    val token: String,
    val msg:String,
    val userid: String?=null
)