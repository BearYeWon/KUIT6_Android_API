package com.example.kuit6_android_api.data.model.request

import kotlinx.serialization.SerialName

data class PostCreateRequest(
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("imageUrl") val imageUrl: String?,
)