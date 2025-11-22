package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PostRepository {
    override suspend fun getPosts(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun createPost(
        author: String,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.createPost(author, request)

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 작성 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun getPostDetail(id: Long): Result<PostResponse> {
        return runCatching {
            val response = apiService.getPostDetail(id)

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun updatePost(id: Long, request: PostCreateRequest): Result<PostResponse> {
        return runCatching {
            val response = apiService.updatePost(id, request)
            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "게시글 수정 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun deletePost(id: Long): Result<Unit> {
        return runCatching {
            val response = apiService.deletePost(id)
            if (response.success) Unit
            else throw Exception(response.message ?: "게시글 삭제 실패")
        }.onFailure { error->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun uploadImage(file: MultipartBody.Part): Result<Map<String, String>> {
        return runCatching {
            val response = apiService.uploadImage(file)
            if(response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "이미지 업로드 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository", error.message.toString())
        }
    }
}