package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.request.LoginRequest
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.LoginResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // 게시글 목록 조회
    @GET("/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    // 게시글 생성
    @POST("/api/posts")
    suspend fun createPost(
        @Query("author") author: String = "규빈",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    // 게시글 상세 조회
    @GET("/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    // 게시글 수정
    @PUT("/api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    // 게시글 삭제
    @DELETE("/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): BaseResponse<Unit>

    // 이미지 업로드
    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): BaseResponse<Map<String, String>>

    @POST("/api/auth/signup")
    suspend fun signup(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @GET("/api/auth/validate")
    suspend fun validateToken(): BaseResponse<Boolean>
}