package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody

interface PostRepository{
    suspend fun getPosts(): Result<List<PostResponse>>
    suspend fun createPost(
        author: String,
        request: PostCreateRequest
    ): Result<PostResponse>
    suspend fun getPostDetail(
        id: Long
    ): Result<PostResponse>
    suspend fun updatePost(
        id: Long,
        request: PostCreateRequest
    ): Result<PostResponse>
    suspend fun deletePost(
        id: Long
    ): Result<Unit>
    suspend fun uploadImage(
        file: MultipartBody.Part
    ): Result<Map<String, String>>
}