package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kuit6_android_api.data.api.RetrofitClient.apiService
import com.example.kuit6_android_api.data.model.response.BaseResponse
import kotlinx.coroutines.flow.first

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
// 자동 로그인 정보 키
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

class TokenRepositoryImpl: TokenRepository {
    override suspend fun saveToken(context: Context, token: String) {
        context.datastore.edit {
            it[KEY] = token
        }
    }

    override suspend fun getToken(context: Context): String? {
        val prefs = context.datastore.data.first()
        return prefs[KEY]
    }
    // 자동 로그인 정보 저장
    override suspend fun saveAutoLogin(context: Context, enabled: Boolean) {
        context.datastore.edit { prefs ->
            prefs[AUTO_LOGIN_KEY] = enabled
        }
    }
    // 자동 로그인되어 있는지 가져오는 함수
    override suspend fun getAutoLogin(context: Context): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }
    // 토큰 검증 api 호출하는 함수
    override suspend fun getValidateTokenApi(): Result<BaseResponse<Boolean>> {
        return runCatching {
            // api로부터 토큰 검증 response 받아오기 (분리)
            val response = apiService.validateToken()

            if(response.success){
                response
            }
            else{
                throw Exception(response.message ?: "api 호출 실패")
            }
        }
    }

    // 토큰 검증 함수
    override suspend fun validateToken(context: Context): Result<Boolean> {
        val response = getValidateTokenApi()

        return response.fold(
            onSuccess = { baseResponse ->
                if (baseResponse.data == true) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(baseResponse.message ?: "토큰 검증 실패"))
                }
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    override suspend fun deleteToken(context: Context) {
        context.datastore.edit { preferences ->
            preferences.remove(KEY)
        }
    }
}