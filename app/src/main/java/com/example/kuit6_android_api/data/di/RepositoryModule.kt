package com.example.kuit6_android_api.data.di

import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.LoginRepositoryImpl
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.repository.PostRepositoryImpl
import com.example.kuit6_android_api.data.repository.TokenApiRepository
import com.example.kuit6_android_api.data.repository.TokenApiRepositoryImpl
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        impl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindTokenApiRepository(
        impl: TokenApiRepositoryImpl
    ): TokenApiRepository
}