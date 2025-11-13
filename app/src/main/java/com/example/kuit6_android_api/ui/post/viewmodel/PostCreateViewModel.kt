package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostCreateUiState
import com.example.kuit6_android_api.ui.post.state.UploadImageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PostCreateViewModel (
    private val postRepository : PostRepository
) : ViewModel(){
    // PostCreateUiState
    private val _uiState = MutableStateFlow<PostCreateUiState>(PostCreateUiState.Loading) // 변경 가능 상태
    val uiState: StateFlow<PostCreateUiState> = _uiState.asStateFlow()

    // UploadImageUiState
    private val _uploadImageUiState = MutableStateFlow<UploadImageUiState>(UploadImageUiState.Idle)
    val uploadImageUiState: StateFlow<UploadImageUiState> = _uploadImageUiState.asStateFlow()

    // 게시글 작성할 때 호출할 함수
    fun createPost(
        author: String,
        request: PostCreateRequest
    ){
        viewModelScope.launch {
            _uiState.value = PostCreateUiState.Loading

            // 레포지토리의 createPost 함수 호출 -> 성공 시 Success(post) 로 uiState에 반환
            postRepository.createPost(author, request)
                .onSuccess { post ->
                    _uiState.value = PostCreateUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostCreateUiState.Error(
                        error.message ?: "error"
                    )
                }
        }
    }

    // 이미지 업로드할 때 호출할 함수
    fun uploadImage(
        file: MultipartBody.Part
    ){
        viewModelScope.launch {
            _uploadImageUiState.value = UploadImageUiState.Loading

            // 레포지토리의 uploadImage() 함수 호출 -> 성공 시 Success(data)로 uiState에 반환
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

    // 이미지 Url 비워줄 때 호출하는 함수 -> uiState를 Idle로 단순 변경
    fun clearUploadedImageUrl() {
        _uploadImageUiState.value = UploadImageUiState.Idle
    }
}
