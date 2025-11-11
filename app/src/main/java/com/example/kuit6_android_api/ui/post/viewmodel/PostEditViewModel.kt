package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import com.example.kuit6_android_api.ui.post.state.UploadImageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PostEditViewModel (
    private val postRepository : PostRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<PostEditUiState>(PostEditUiState.Loading) // 변경 가능 상태
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    private val _uploadImageUiState = MutableStateFlow<UploadImageUiState>(UploadImageUiState.Idle)
    val uploadImageUiState: StateFlow<UploadImageUiState> = _uploadImageUiState.asStateFlow()

    fun editPost(
        postId: Long,
        request: PostCreateRequest
    ){
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading

            postRepository.updatePost(postId, request)
                .onSuccess { post ->
                    _uiState.value = PostEditUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        error.message ?: "error"
                    )
                }
        }
    }

    fun uploadImage(
        file: MultipartBody.Part
    ){
        viewModelScope.launch {
            _uploadImageUiState.value = UploadImageUiState.Loading

            postRepository.uploadImage(file)
                .onSuccess { data ->
                    _uploadImageUiState.value = UploadImageUiState.Success(data)
                }
                .onFailure { error ->
                    _uploadImageUiState.value =
                        UploadImageUiState.Error(error.message ?: "이미지 업로드 실패")
                }
        }
    }

    fun clearUploadedImageUrl() {
        _uploadImageUiState.value = UploadImageUiState.Idle
    }

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading

            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.value = PostEditUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(error.message ?: "게시글 불러오기 실패")
                }
        }
    }
}