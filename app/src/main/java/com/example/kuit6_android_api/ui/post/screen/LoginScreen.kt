package com.example.kuit6_android_api.ui.post.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kuit6_android_api.ui.post.state.TokenValidationState
import com.example.kuit6_android_api.ui.post.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val buttonText = when (uiState.tokenValidationState) {
        TokenValidationState.Initial -> if (uiState.isLoading) "검증 중..." else "토큰 검증"
        TokenValidationState.Success -> "토큰 검증 성공"
        TokenValidationState.Failure -> "토큰 검증 실패"
    }

    // 스크린 진입 시 자동 로그인 상태 불러오기 -> 자동 로그인 true면 자동으로 토큰 검증까지
    LaunchedEffect(Unit) {
        viewModel.initAutoLogin()
    }

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.id,
                onValueChange = {
                    viewModel.onIdChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("아이디") }
            )

            TextField(
                value = uiState.password,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("비밀번호") }
            )

            Row(
                Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = uiState.isAutoLogin,
                    onCheckedChange = {
                        viewModel.onAutoLoginChanged(it)
                    }
                )
                Text("자동 로그인")
            }

            Row() {
                Button(onClick = {
                    viewModel.login()
                }) {
                    Text("로그인")
                }
                Button(onClick = {
                    viewModel.signup()
                }) {
                    Text("회원가입")
                }
            }
            Text("토큰 : ${uiState.token}")
            Button(onClick = {
                viewModel.getToken()
            }) {
                Text("토큰 조회")
            }

            Button(onClick = {
                // 토큰 검증
                scope.launch {
                    viewModel.validateToken()
                }
            }) {
                Text(buttonText)
            }
        }
    }
}