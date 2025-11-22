package com.example.kuit6_android_api.data.repository

interface TokenRepository {
    // 토큰 저장 함수
    suspend fun saveToken(token: String)
    // 토큰 가져오는 함수
    suspend fun getToken(): String? // nullable 로 반환 타입 정의
    // 자동 로그인 정보 저장 함수
    suspend fun saveAutoLogin(enabled: Boolean)
    // 자동 로그인되어 있는지 가져오는 함수
    suspend fun getAutoLogin(): Boolean
    // 토큰 삭제하는 함수
    suspend fun deleteToken()
}