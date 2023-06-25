package uz.gita.dima.waziypalar.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.util.Constants.ANDROID_OREO
import uz.gita.dima.waziypalar.util.Constants.DEVICE_ANDROID_VERSION
import uz.gita.dima.waziypalar.util.Constants.NOTIFICATION_CHANNEL_ID
import uz.gita.dima.waziypalar.util.Constants.NOTIFICATION_CHANNEL_NAME
import uz.gita.dima.waziypalar.util.Constants.NOTIFICATION_ID
import javax.inject.Inject


class Notify @Inject constructor(
    private val notificationManager: NotificationManager,
    private val notificationBuilder: NotificationCompat.Builder
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(
        context: Context,
        init: NotificationData.() -> Unit
    ): NotificationCompat.Builder {
        val data = NotificationData()
        data.init()

        val notificationId = data.notificationId ?: NOTIFICATION_ID.hashCode()

        if (DEVICE_ANDROID_VERSION >= ANDROID_OREO)
            createNotification(context, notificationManager)

        notificationBuilder.apply {
            setContentTitle(data.notificationTitle)
            setContentText(data.notificationBody)
            setAutoCancel(data.isAutoCancelable)
            setContentIntent(data.pendingIntent)
            setStyle(NotificationCompat.BigTextStyle().bigText(data.notificationBody))
        }

        notificationManager.notify(notificationId, notificationBuilder.build())

        return notificationBuilder
    }


    fun showProgressNotification(
        context: Context,
        max: Int = 100,
        progress: Int,
        isIndeterminate: Boolean = false,
        init: NotificationData.() -> Unit
    ) {
        val data = NotificationData()
        data.init()

        val notificationId = data.notificationId ?: NOTIFICATION_ID.hashCode()

        if (DEVICE_ANDROID_VERSION >= ANDROID_OREO)
            createNotification(context, notificationManager)

        notificationBuilder.apply {
            setContentTitle("Uploading...")
            setProgress(max, progress, isIndeterminate)
            setOngoing(true)
            setAutoCancel(false)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(context: Context, notificationManager: NotificationManager) {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setSound(uri, attributes)
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }


    data class NotificationData(
        var notificationId: Int? = null,
        var notificationTitle: String = "",
        var notificationBody: String = "",
        var pendingIntent: PendingIntent? = null,
        var isAutoCancelable: Boolean = true,
        @DrawableRes var smallIcon: Int? = null
    )

    companion object {

    }
}