package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PostViewModel : ViewModel() {
    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    var isUploading by mutableStateOf(false)
        private set

    private val apiService = RetrofitClient.apiService

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            runCatching {
                apiService.getPostDetail(postId)
            }.onSuccess { response ->
                if (response.success && response.data != null) {
                    postDetail = response.data
                }
            }.onFailure { error ->
                // 에러 처리
                postDetail = null
            }
        }
    }

    fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.createPost(author, request)
            }.onSuccess { response ->
                if (response.success) {
                    // 이미지 업로드 관련 코드
                    clearUploadedImageUrl()
                    onSuccess()
                }
            }
        }
    }

    fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.updatePost(postId, request)
            }.onSuccess { response ->
                if (response.success) {
                    clearUploadedImageUrl()
                    onSuccess()
                }
            }.onFailure { error ->
                // 에러 처리
            }
        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    // 이미지 업로드 함수
    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isUploading = true
            runCatching {
                val file = UriUtils.uriToFile(context, uri)
                if (file == null) {
                    throw Exception("파일 변환 실패")
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                apiService.uploadImage(body)
            }.onSuccess { response ->
                isUploading = false
                if (response.success && response.data != null) {
                    val imageUrl = response.data["imageUrl"]
                    if (imageUrl != null) {
                        uploadedImageUrl = imageUrl
                        onSuccess(imageUrl)
                    }
                }
            }.onFailure { error ->
                isUploading = false
                onError(error.message ?: "업로드 실패")
            }
        }
    }
}