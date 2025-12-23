package com.pinup.placePinup

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.util.Const


class MainActivity : ComponentActivity() {
    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"
    }

    private val registerForActivityResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key }
        when {
            deniedPermissionList.isNotEmpty() -> {
                val map = deniedPermissionList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                map[DENIED]?.let {
                    hLog("알림 거부")
                }
                map[EXPLAINED]?.let {
                    // 권한 요청이 완전히 막혔을 때(주로 앱 상세 창 열기)
                }
            }
            else -> {
                hLog("알림 허용")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextFactory = ContextFactory(this)
        val data: Uri? = intent?.data

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult.launch(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            )
        }

        setContent {
            PinUpApp(
                userId = data?.getQueryParameter(Const.ShareKey.KAKAO_USER_ID)?.toInt() ?: -1,
                contextFactory = contextFactory
            )
        }
    }
}