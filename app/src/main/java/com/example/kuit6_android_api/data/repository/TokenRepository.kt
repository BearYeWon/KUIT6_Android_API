package com.example.kuit6_android_api.data.repository

import android.content.Context
import com.example.kuit6_android_api.data.model.response.BaseResponse

interface TokenRepository {
    // 토큰 저장 함수
    suspend fun saveToken(context: Context, token: String)
    // 토큰 가져오는 함수
    suspend fun getToken(context: Context): String? // nullable 로 반환 타입 정의
    // 자동 로그인 정보 저장 함수
    suspend fun saveAutoLogin(context: Context, enabled: Boolean)
    // 자동 로그인되어 있는지 가져오는 함수
    suspend fun getAutoLogin(context: Context): Boolean
    // api 호출 함수
    suspend fun getValidateTokenApi(): Result<BaseResponse<Boolean>>
    // 토큰 검증 함수
    suspend fun validateToken(context: Context): Result<Boolean>
    // 토큰 삭제하는 함수
    suspend fun deleteToken(context: Context)
}