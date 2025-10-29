package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class PostViewModel : ViewModel() {

    private var nextId = 4L

    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    var isUploading by mutableStateOf(false)
        private set

    private val apiService = RetrofitClient.apiService
    fun getPosts() {
        viewModelScope.launch{
            runCatching {
                apiService.getPosts()
            }.onSuccess { response ->
                if(response.success && response.data != null)
                    posts = response.data
            }
        }
    }

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            runCatching {
                apiService.getPostDetail(postId)
            }.onSuccess { response ->
                if(response.success && response.data != null)
                    postDetail = response.data
            }
        }
    }

    fun createPost(
        author: String = "anonymous",
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val finalImageUrl = imageUrl ?: uploadedImageUrl
                val request = PostCreateRequest(title, content, finalImageUrl)
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

    // 게시물 수정 시 호출
    fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val finalImageUrl = imageUrl ?: uploadedImageUrl // imageUrl이 없으면 uploadImage()에서 정해진 uploadedImageUrl 가져옴
                // 요청 body로 보낼 DTO 인스턴스 만들기
                val request = PostCreateRequest(title, content, finalImageUrl)
                apiService.updatePost(postId, request)
            }.onSuccess { response ->
                if(response.success && response.data != null){
                    // 변경된 새 리스트를 posts에 대입
                    posts = posts.map{
                        // 순회 중인 원소의 id == postId면 갱신된 객체 response.data로 교체
                        if(it.id == postId) response.data else it
                    }
                    postDetail = response.data // postDetail을 갱신된 객체로 바꾸기
                    onSuccess()
                }
            }
        }
    }

    // 삭제 시 호출
    fun deletePost(
        postId: Long,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                apiService.deletePost(postId)
            }.onSuccess { response ->
                if(response.success){
                    posts = posts.filterNot { it.id == postId }
                    onSuccess()
                }
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val fileName = getFileName(context, uri) ?: "image_${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // URI로부터 파일 이름 가져오기
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    // 이미지 업로드 시 호출
    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isUploading = true
            runCatching {
                val file = uriToFile(context, uri) // uri를 실제 파일로 변환
                if (file == null) {
                    throw Exception("파일 변환 실패")
                }

                // 파일 내용을 RequestBody로 감쌈
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                // Part 생성
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

    //이미지 삭제 버튼 누를 시 호출
    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    fun clearPostImage() {
        postDetail = postDetail?.copy(imageUrl = null)
    }

    private fun getCurrentDateTime(): String {
        return LocalDateTime.now().toString()
    }
}
