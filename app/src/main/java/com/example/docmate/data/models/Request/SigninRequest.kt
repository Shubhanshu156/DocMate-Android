package com.example.docmate.data.models.Request



data class AuthRequest(
    val password: String,
    val type: String,
    val username: String
)