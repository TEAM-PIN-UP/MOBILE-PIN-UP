package com.pinup.placePinup.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pinup.placePinup.MainActivity
import com.pinup.placePinup.R
import com.pinup.placePinup.platform.FcmBridgeStore
import com.pinup.placePinup.platform.hLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class PinUpFCM : FirebaseMessagingService() {
    companion object {
        const val PUSH_TYPE = "push_type"
        const val PUSH_TARGET_ID = "push_targetId"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FcmBridgeStore.setFcmToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        hLog(data.toString())

        val body = data["body"] ?: ""
        val type = data["type"] ?: ""
        val targetId = data["targetId"]?.toInt() ?: -1
        val imageUrl = data["imageUrl"] ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            showNotification(
                body = body,
                type = type,
                targetId = targetId,
            )
        }
    }

    private fun showNotification(
        body: String,
        type: String,
        targetId: Int,
    ) {
        val channelId = "pinup_push"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "PinUp 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                // 잠금화면에서 내용 공개(가능한 범위 내)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                description = "PinUp 푸시 알림"
            }
            nm.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(PUSH_TYPE, type)
            putExtra(PUSH_TARGET_ID, targetId)

            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("핀업")
            .setContentText(body)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        nm.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notification.build())
    }
}