package com.example.kuit6_android_api.data.di

import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.LoginRepositoryImpl
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.repository.PostRepositoryImpl
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl

class AppContainer {
    private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }

    val postRepository: PostRepository by lazy{
        PostRepositoryImpl(apiService)
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(apiService)
    }

    val tokenRepository: TokenRepository by lazy {
        TokenRepositoryImpl()
    }
}