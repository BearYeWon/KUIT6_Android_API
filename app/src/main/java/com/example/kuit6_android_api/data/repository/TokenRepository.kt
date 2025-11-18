package com.example.kuit6_android_api.data.repository

import android.content.Context

interface TokenRepository {
    suspend fun saveToken(context: Context, token: String)

    suspend fun getToken(context: Context): String? // nullable 로 반환 타입 정의
    // 자동 로그인 함수
    suspend fun saveAutoLogin(context: Context, enabled: Boolean)
    suspend fun getAutoLogin(context: Context): Boolean
    // 자동 로그인 검증
    suspend fun validateToken(context: Context): Result<Boolean>

    suspend fun deleteToken(context: Context)
}