package uz.gita.dima.waziypalar.utils

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.receiver.AlarmReceiver
import uz.gita.dima.waziypalar.receiver.AlarmReceiver.Companion.ALARM_INTENT_ACTION
import uz.gita.dima.waziypalar.utils.Constants.ALARM_DESCRIPTION
import uz.gita.dima.waziypalar.utils.Constants.ALARM_ID
import uz.gita.dima.waziypalar.utils.Constants.ALARM_TEXT

/** Holds the extension methods for starting & cancelling alarm from Activity/Fragment  */

@ExperimentalCoroutinesApi
fun Context.startAlarmedNotification(
    id: String,
    body: String,
    description: String,
    dateTime: Long,
    alarmManager: AlarmManager
) {
    val intent = Intent(this, AlarmReceiver::class.java).apply {
        action = ALARM_INTENT_ACTION
        this.putExtra(ALARM_TEXT, body)
        this.putExtra(ALARM_DESCRIPTION, description)
        this.putExtra(ALARM_ID, id)
    }

    val pendingIntent =
        PendingIntent.getBroadcast(this, id.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)

    alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, dateTime, pendingIntent)
}

@ExperimentalCoroutinesApi
fun Context.cancelAlarmedNotification(requestCode: String) {
    val intent = Intent(this, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            this,
            requestCode.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    pendingIntent.cancel()
}
