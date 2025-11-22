package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
// 자동 로그인 정보 키
val AUTO_LOGIN_KEY = booleanPreferencesKey("auto_login")

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TokenRepository {

    override suspend fun saveToken(token: String) {
        context.datastore.edit {
            it[KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        val prefs = context.datastore.data.first()
        return prefs[KEY]
    }
    // 자동 로그인 정보 저장
    override suspend fun saveAutoLogin(enabled: Boolean) {
        context.datastore.edit { prefs ->
            prefs[AUTO_LOGIN_KEY] = enabled
        }
    }
    // 자동 로그인되어 있는지 가져오는 함수
    override suspend fun getAutoLogin(): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }

    override suspend fun deleteToken() {
        context.datastore.edit { preferences ->
            preferences.remove(KEY)
        }
    }
}