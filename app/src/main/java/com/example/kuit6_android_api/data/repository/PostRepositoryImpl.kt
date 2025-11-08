package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.response.PostResponse

class PostRepositoryImpl(
    private val apiService: ApiService
) : PostRepository{
    override suspend fun getPosts(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository", error.message.toString())
        }
    }
}