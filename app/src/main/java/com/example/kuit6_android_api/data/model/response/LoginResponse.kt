package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String
)
