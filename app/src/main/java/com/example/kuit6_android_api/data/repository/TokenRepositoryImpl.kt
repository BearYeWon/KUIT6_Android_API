package com.example.kuit6_android_api.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kuit6_android_api.data.api.RetrofitClient.apiService
import kotlinx.coroutines.flow.first

val Context.datastore: DataStore<Preferences> by preferencesDataStore("Token")
val KEY = stringPreferencesKey("token")
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

    override suspend fun saveAutoLogin(context: Context, enabled: Boolean) {
        context.datastore.edit { prefs ->
            prefs[AUTO_LOGIN_KEY] = enabled
        }
    }

    override suspend fun getAutoLogin(context: Context): Boolean {
        val prefs = context.datastore.data.first()
        return prefs[AUTO_LOGIN_KEY] ?: false
    }

    override suspend fun validateToken(context: Context): Result<Boolean> {
        return runCatching {
            val response = apiService.validateToken()

            if(response.success && response.data == true){
                true
            }
            else{
                throw Exception(response.message ?: "토큰 검증 실패")
            }
        }
    }

    override suspend fun deleteToken(context: Context) {
        context.datastore.edit { preferences ->
            preferences.remove(KEY)
        }
    }
}