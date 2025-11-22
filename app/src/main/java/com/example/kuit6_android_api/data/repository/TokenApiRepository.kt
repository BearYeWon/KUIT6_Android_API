package com.example.kuit6_android_api.data.repository

interface TokenApiRepository{
    // 토큰 검증 함수
    suspend fun validateToken(): Result<Boolean>
}