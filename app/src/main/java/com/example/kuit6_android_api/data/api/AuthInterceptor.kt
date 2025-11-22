package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking {
            tokenRepository.getToken()
        }

        val request = chain.request().newBuilder()
        if(!token.isNullOrEmpty()){
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}