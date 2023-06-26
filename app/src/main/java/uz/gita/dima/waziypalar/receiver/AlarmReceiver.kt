package uz.gita.dima.waziypalar.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.service.TaskStatusUpdateService
import uz.gita.dima.waziypalar.utils.Constants
import uz.gita.dima.waziypalar.utils.Constants.ALARM_DESCRIPTION
import uz.gita.dima.waziypalar.utils.Constants.ALARM_ID
import uz.gita.dima.waziypalar.utils.Constants.ALARM_TEXT
import uz.gita.dima.waziypalar.utils.Constants.NOTIFICATION_CHANNEL_ID
import uz.gita.dima.waziypalar.utils.Constants.NOTIFICATION_CHANNEL_NAME
import uz.gita.dima.waziypalar.utils.Constants.NOTIFICATION_ID
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.view.fragment.TaskEditFragmentArgs
import javax.inject.Inject


/** A [HiltBroadcastReceiver] receiver to send notification of task triggered by AlarmManager */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var alarmText = ""
    private var alarmDescription = ""
    private var taskId = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if ((intent?.action == ALARM_INTENT_ACTION
                    || intent?.action == "android.intent.action.BOOT_COMPLETED")
            && context != null
        ) {
            alarmText = "${intent.extras?.getString(ALARM_TEXT)}"
            alarmDescription = "${intent.extras?.getString(ALARM_DESCRIPTION)}"
            taskId = "${intent.extras?.getString(ALARM_ID)}"

            showNotification(context, alarmText, alarmDescription, taskId)
        } else return
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(
        context: Context,
        alarmText: String,
        alarmDescription: String,
        taskId: String
    ) {

        // Generates notification code taskId.hashCode()
        val uniqueNotificationId = taskId.hashCode()
        // Extras for notification action button
        val intent = Intent(context, TaskStatusUpdateService::class.java).also {
            it.putExtra(ALARM_ID, taskId)
            it.putExtra(NOTIFICATION_ID, uniqueNotificationId)
        }

        val actionIntent = PendingIntent.getActivity(
            context, uniqueNotificationId, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val contentArgs = TaskEditFragmentArgs(taskId).toBundle()
        val contentIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskEditFragment)
            .setArguments(contentArgs)
            .createPendingIntent()

        // Creating notification channel
        if (Constants.DEVICE_ANDROID_VERSION >= Constants.ANDROID_OREO)
            createNotification(context, notificationManager)

        baseNotificationBuilder.apply {
            setContentTitle(alarmText)
            setContentText(alarmDescription)
            setContentIntent(contentIntent)
            setStyle(NotificationCompat.BigTextStyle().bigText(alarmDescription))
//            addAction(R.drawable.ic_notification, "Mark Complete", actionIntent)
        }

        notificationManager.notify(uniqueNotificationId, baseNotificationBuilder.build())
    }

    /**
     * This method creates the notification channel with custom sound attribute
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(context: Context, notificationManager: NotificationManager) {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setSound(uri, attributes)
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val ALARM_INTENT_ACTION = "alarm_broadcast_action"
    }
}