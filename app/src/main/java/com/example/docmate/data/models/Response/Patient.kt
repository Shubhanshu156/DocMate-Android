package com.example.docmate.data.models.Response

data class Patient(
    val username:String?=null,
    val name: String?=null,
    val age: Int?=null,
    val gender: String?=null,
    val contactNumber: String?=null,
    val email: String?=null,
    val address: String?=null,
    val profileurl:String?=null,
    val medicalHistory: List<String>?=null,
)