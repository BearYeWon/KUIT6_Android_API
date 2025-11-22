package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import com.example.kuit6_android_api.ui.post.state.UploadImageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor (
    private val postRepository : PostRepository
) : ViewModel(){
    // PostEditUiState
    private val _uiState = MutableStateFlow<PostEditUiState>(PostEditUiState.Loading) // 변경 가능 상태
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    // UploadImageUiSate
    private val _uploadImageUiState = MutableStateFlow<UploadImageUiState>(UploadImageUiState.Idle)
    val uploadImageUiState: StateFlow<UploadImageUiState> = _uploadImageUiState.asStateFlow()

    // 게시글 수정 시 호출하는 함수 -> 성공 시 반환되는 response를 Sucess(pot)로 uiState에 반환
    fun editPost(
        postId: Long,
        request: PostCreateRequest
    ){
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading

            // 레포지토리의 updatePost() 호출해서 response 반환 -> post 반환됨 -> UiState로 넘김
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

    // 이미지 업로드할 때 호출하는 함수 (PostCreateViewModel과 동일)
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

    // 수정 후 popBackStack -> 디테일 스크린으로 넘어감 -> getPostDetail 호출 (PostDetailViewModel에서와 동일)
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