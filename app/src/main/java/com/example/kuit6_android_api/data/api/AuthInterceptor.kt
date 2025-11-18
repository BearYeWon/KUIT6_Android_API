package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.data.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,

    private val tokenRepository: TokenRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            tokenRepository.getToken(context)
        }

        // 토큰이 없을 시 그냥 진행
        if(token.isNullOrEmpty())
            return chain.proceed(originalRequest)

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}