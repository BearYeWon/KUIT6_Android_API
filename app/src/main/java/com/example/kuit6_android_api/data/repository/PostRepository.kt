package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.response.PostResponse

interface PostRepository{
    suspend fun getPosts(): Result<List<PostResponse>>

}