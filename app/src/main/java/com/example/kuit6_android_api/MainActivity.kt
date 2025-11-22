package com.example.kuit6_android_api

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.kuit6_android_api.ui.navigation.NavGraph
import com.example.kuit6_android_api.ui.navigation.PostListRoute
import com.example.kuit6_android_api.ui.theme.KUIT6_Android_APITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // di 주입 명시
class MainActivity : ComponentActivity() {

    // 권한 요청 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "갤러리 접근 권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "갤러리 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 갤러리 권한 요청
        checkAndRequestPermission()

        setContent {
            KUIT6_Android_APITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        startDestination = PostListRoute
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            // Android 12 이하
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 이미 권한이 있음
            }
            shouldShowRequestPermissionRationale(permission) -> {
                // 권한 거부 이력이 있음 - 설명 표시 후 재요청
                Toast.makeText(
                    this,
                    "이미지를 선택하려면 갤러리 접근 권한이 필요합니다",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                // 권한 요청
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}
