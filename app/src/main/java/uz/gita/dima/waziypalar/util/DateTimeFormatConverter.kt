package uz.gita.dima.waziypalar.util

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import uz.gita.dima.waziypalar.util.Constants.DEFAULT_TASK_TIME
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Holds all the extension methods for handeling date time format & comaparing */

@RequiresApi(Build.VERSION_CODES.O)
fun String.compareWithToday(): Int {
    val taskDate = this.toLocalDateTime()
    val currentDate = LocalDateTime.now()
    taskDate?.let {
        return when {
            taskDate.isEqual(currentDate) -> Constants.IS_EQUAL
            taskDate.isBefore(currentDate) -> Constants.IS_BEFORE
            taskDate.isAfter(currentDate) -> Constants.IS_AFTER
            else -> Constants.IS_ERROR
        }
    }
    return 2
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime? {
    return if (this.isNotEmpty()) {
        val epochValue = this.toLong()
        Instant.ofEpochMilli(epochValue).atZone(ZoneId.systemDefault()).toLocalDateTime()
    } else Instant.ofEpochMilli(DEFAULT_TASK_TIME).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.beautifyDateTime(): String {
    val dateTime = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")
    return this.format(dateTime)
}


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.showDayDateAndMonth(): String? {
    val dayDateMonth = DateTimeFormatter.ofPattern("EEEE dd, MMM")
    return this.format(dayDateMonth)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.showTime(): String? {
    val dateTime = DateTimeFormatter.ofPattern("hh:mm a")
    return this.format(dateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun Long.convertFromEpochTime(): String {
    val timeNow = System.currentTimeMillis()
    val givenTime = this
    val timeDayRelative = DateUtils.getRelativeTimeSpanString(
        givenTime,
        timeNow,
        DateUtils.DAY_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    )
    val hourFormatter = SimpleDateFormat("HH:mm a")
    val timeHour = hourFormatter.format(givenTime)
    return "$timeDayRelative at $timeHour"
}