package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class BaseResponse<T>(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String?,
    @SerialName("data") val data: T?,
    @SerialName("timestamp") val timestamp: String
)