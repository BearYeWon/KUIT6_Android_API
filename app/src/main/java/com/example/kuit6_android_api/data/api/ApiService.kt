package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
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
    @GET("/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    @POST("/api/posts")
    suspend fun createPost(
        @Query("author") author: String = "예원",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    // DELETE 요청
    @DELETE("/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): BaseResponse<Unit> // data에 빈 객체 반환

    @GET("/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    // 업데이트 요청
    @PUT("/api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body request: PostCreateRequest // 수정된 내용 전달
    ): BaseResponse<PostResponse> // 수정된 내용이 data에 들어가고, 반환

    // Retrofit에서 이미지 파일을 multipart/form-data로 업로드
    @Multipart // multipart?form-data 형식의 요청을 보냄
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part // multipart의 한 조각(part) 타입
    ):BaseResponse<Map<String, String>> // 본문 data가 key-value의 Map 형식 -> ex) "url":"..."
}