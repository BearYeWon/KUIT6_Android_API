package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.response.LoginResponse

interface LoginRepository {
    suspend fun signup(
        id: String,
        password: String
    ): Result<LoginResponse>

    suspend fun login(
        id: String,
        password: String
    ): Result<LoginResponse>
}