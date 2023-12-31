package uz.gita.dima.waziypalar.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import javax.inject.Inject

/** This opens dateTimepicker to select date & time from Activity/Fragment */

class DateTimePicker @Inject constructor(private val currentDateTime: Calendar) {

    private var _epochFormat = MutableLiveData<Long>()
    val epochFormat: LiveData<Long>
        get() = _epochFormat

    fun openDateTimePicker(context: Context) {
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMin = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val pickDateTime = Calendar.getInstance()
                pickDateTime.set(year, month, day, hour, minute)
                _epochFormat.value = pickDateTime.time.time
            }, startHour, startMin, true).show()
        }, startYear, startMonth, startDay).show()
    }
}