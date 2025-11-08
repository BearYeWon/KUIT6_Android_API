package com.example.kuit6_android_api

import android.app.Application
import com.example.kuit6_android_api.data.di.AppContainer

class App : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}