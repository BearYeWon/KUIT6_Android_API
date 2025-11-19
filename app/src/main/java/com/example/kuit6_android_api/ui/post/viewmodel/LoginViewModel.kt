package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import com.example.kuit6_android_api.ui.post.state.TokenValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdChanged(id: String) {
        _uiState.update { it.copy(id = id) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onAutoLoginChanged(context: Context, isAutoLogin: Boolean) {
        _uiState.update { it.copy(isAutoLogin = isAutoLogin) }
        viewModelScope.launch {
            tokenRepository.saveAutoLogin(context, isAutoLogin)
        }
    }

    // 회원가입 함수
    fun signup(context: Context) {
        viewModelScope.launch {
            // 레포지토리에서 signup() 함수 호출
            loginRepository.signup(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(context, it.token) // 회원가입 성공 시 토큰 정보 저장
            }
        }
    }

    // 로그인 함수
    fun login(context: Context) {
        viewModelScope.launch {
            // 레포지토리의 login() 함수 호출
            loginRepository.login(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(context, it.token) // 성공 시 토큰 정보 저장
            }
        }
    }

    // 토큰 가져오는 함수
    fun getToken(context: Context) {
        viewModelScope.launch {
            val token = tokenRepository.getToken(context) // 토큰 레포에서 getToken()으로 토큰 가져오기
            _uiState.update { it.copy(token = token ?: "") }
        }
    }

    // 자동 로그인 초기화(LoginScreen의 LaunchedEffect에서 호출)
    fun initAutoLogin(context: Context) {
        viewModelScope.launch {
            // DataStore의 자동 로그인 값 가져옴
            val isAuto = tokenRepository.getAutoLogin(context) // 자동 로그인 정보 가져오기

            // UI 상태에 반영
            _uiState.update { it.copy(isAutoLogin = isAuto) }

            // 자동 로그인 설정이 True이면 자동 토큰 검증 시도
            if (isAuto && _uiState.value.tokenValidationState == TokenValidationState.Initial) {
                // isAuto가 true일 때만 자동 검증 시도
                validateToken(context)
            }
        }
    }

    // 토큰 검증 (버튼 클릭 및 자동 검증에 사용)
    fun validateToken(context: Context) {
        viewModelScope.launch {
            // 검증 시작 상태 업데이트
            _uiState.update {
                it.copy(
                    isLoading = true,
                    tokenValidationState = TokenValidationState.Initial // 로딩 중 UI 상태 전환을 위해 Initial로 설정
                )
            }

            // 토큰 검증 API 호출
            val result = tokenRepository.validateToken(context)

            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { isValid ->
                // 검증 성공
                if (isValid) {
                    _uiState.update {
                        it.copy(
                            tokenValidationState = TokenValidationState.Success
                        )
                    }
                } else { // 검증 실패
                    tokenRepository.deleteToken(context) // 토큰 삭제
                    tokenRepository.saveAutoLogin(context, false) // 자동 로그인 해제
                    _uiState.update {
                        it.copy(
                            tokenValidationState = TokenValidationState.Failure,
                            isAutoLogin = false // UI에도 반영
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        tokenValidationState = TokenValidationState.Failure,
                    )
                }
            }
        }
    }
}