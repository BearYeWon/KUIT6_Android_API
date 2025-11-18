package com.example.kuit6_android_api.data.api

import android.annotation.SuppressLint
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.BuildConfig
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @SuppressLint("StaticFieldLeak")
    private val authInterceptor = AuthInterceptor(
        App.instance.applicationContext,
        TokenRepositoryImpl()
    )

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true  // 서버에서 추가 필드가 와도 무시
        coerceInputValues = true   // null이 와야 할 곳에 다른 값이 와도 처리
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}