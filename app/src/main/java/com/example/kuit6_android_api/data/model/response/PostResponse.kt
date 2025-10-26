package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class PostResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("author") val author: AuthorResponse,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String
)