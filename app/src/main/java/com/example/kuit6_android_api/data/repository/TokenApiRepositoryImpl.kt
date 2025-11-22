package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenApiRepositoryImpl @Inject constructor(
    private val apiServie: ApiService
) : TokenApiRepository {

    override suspend fun validateToken(): Result<Boolean> =
        runCatching {
            val response = apiServie.validateToken()
            if (response.success && response.data == true) {
                true
            } else {
                throw Exception(response.message ?: "토큰 검증 실패")
            }
        }
}